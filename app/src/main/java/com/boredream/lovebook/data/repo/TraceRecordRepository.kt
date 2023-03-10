package com.boredream.lovebook.data.repo

import com.amap.api.mapcore.util.it
import com.blankj.utilcode.util.CollectionUtils
import com.blankj.utilcode.util.LogUtils
import com.boredream.lovebook.base.BaseRepository
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.repo.source.TraceRecordLocalDataSource
import com.boredream.lovebook.data.repo.source.TraceRecordRemoteDataSource
import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.utils.SyncUtils
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

/**
 * 轨迹记录，针对的是整条轨迹线路
 */
@Singleton
class TraceRecordRepository @Inject constructor(
    private val service: ApiService,
    private val remoteDataSource: TraceRecordRemoteDataSource,
    private val localDataSource: TraceRecordLocalDataSource,
) : BaseRepository() {

    suspend fun syncDataPull() {
        // 服务端把本地时间戳之后的所有数据都查询出来，一次性返回给前端
        val localTimestamp = SyncUtils.get() // TODO: SyncUtils 里有context引用

        // 不关注 response
        try {
            val response = service.getTraceRecordListBySyncTimestamp(localTimestamp)
            response.data?.let { traceRecordList ->
                LogUtils.i("pull data size = ${traceRecordList.size}")
                for (traceRecord in traceRecordList) {
                    // 1. 拉取的数据先和本地进行比较
                    val localResponse = localDataSource.getTraceRecordByDbId(traceRecord.dbId)
                    val localRecord = localResponse.data

                    // 2. 如果本地数据不存在，或者已经同步到服务器过了，直接覆盖
                    if (localRecord == null || localRecord.synced) {
                        // 还要拉取location list
                        traceRecord.traceList = remoteDataSource.getTraceLocationList(traceRecord.id!!).data
                        LogUtils.i("pull and save data = ${traceRecord.name} , location size = ${traceRecord.traceList?.size}")
                        SyncUtils.update(traceRecord.syncTimestamp)
                        add(traceRecord)
                    } else {
                        // 3. 如果本地数据，同步标志位是false，即本地有修改还没提交给服务端的，处理冲突
                        // 冲突数据【本地的】，因为大部分是珍贵的轨迹收集数据，所以本地为准，服务端数据抛弃
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun syncDataPush() {
        localDataSource.getUnSyncedTraceRecord().data?.let { traceRecordList ->
            LogUtils.i("getUnSyncedTraceRecord ${traceRecordList.size}")
            traceRecordList.forEach { pushDataToRemote(it) }
        }
    }

    suspend fun getList() = localDataSource.getList()

    suspend fun getLocationList(traceRecordDbId: String) =
        localDataSource.getTraceLocationList(traceRecordDbId)

    suspend fun add(data: TraceRecord) = localDataSource.add(data)

    suspend fun pushDataToRemote(data: TraceRecord): ResponseEntity<TraceRecord> {
        val response = if (data.id != null) {
            remoteDataSource.update(data)
        } else {
            // 如果是add新数据，同时要把location list数据也带着
            if(CollectionUtils.isEmpty(data.traceList)) {
                data.traceList = localDataSource.getTraceLocationList(data.dbId).data
            }
            remoteDataSource.add(data)
        }
        if (response.isSuccess() && response.data != null) {
            LogUtils.i("push success: ${response.data.name} , " +
                    "local update syncTimestamp = ${response.data.syncTimestamp}")
            localDataSource.update(response.data)
            SyncUtils.update(response.data.syncTimestamp)
        }
        return response
    }

    suspend fun update(data: TraceRecord): ResponseEntity<TraceRecord> {
        data.synced = false // 同步标志位，有修改的都需要设为false
        return localDataSource.update(data)
    }

    suspend fun delete(data: TraceRecord): ResponseEntity<TraceRecord> {
        data.synced = false // 同步标志位，有修改的都需要设为false
        return localDataSource.delete(data)
    }

}