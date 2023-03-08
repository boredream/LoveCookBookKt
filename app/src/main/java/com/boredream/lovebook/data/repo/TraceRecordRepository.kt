package com.boredream.lovebook.data.repo

import com.blankj.utilcode.util.LogUtils
import com.boredream.lovebook.base.BaseRepository
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.repo.source.TraceRecordLocalDataSource
import com.boredream.lovebook.data.repo.source.TraceRecordRemoteDataSource
import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.utils.SyncUtils
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 轨迹记录，针对的是整条轨迹线路
 */
@Singleton
class TraceRecordRepository @Inject constructor(
    private val service: ApiService,
    private val remoteDataSource: TraceRecordRemoteDataSource,
    private val localDataSource: TraceRecordLocalDataSource,
) : BaseRepository() {

    private var curPage = 1
    private val pageList = ArrayList<TraceRecord>()

    /**
     * 同步数据，先拉取，再上传
     */
    suspend fun syncData() {
        syncDataPull()
        syncDataPush()
    }

    suspend fun syncDataPull() {
        // 服务端把本地时间戳之后的所有数据都查询出来，一次性返回给前端
        val localTimestamp = SyncUtils.get() // TODO: SyncUtils 里有context引用

        // 不关注 response
        try {
            val response = service.syncTraceRecordList(localTimestamp)
            val traceRecordList = response.data ?: return

            for (traceRecord in traceRecordList) {
                // 1. 拉取的数据先和本地进行比较，id不存在的新数据直接插入，如果是id存在的继续判断
                val localResponse = localDataSource.getTraceRecordById(traceRecord.id!!)
                if (localResponse.data == null) {
                    localDataSource.add(traceRecord)
                    continue
                }

                // 2. 如果本地待更新的这笔数据，同步标志位是false，直接以服务端为准，覆盖即可
                val localRecord = localResponse.data
                if (!localRecord.synced) {
                    localDataSource.add(traceRecord)
                    continue
                }

                // 3. 如果本地待更新的这笔数据，同步标志位是true，即本地有修改还没提交给服务端的，进入到冲突处理
                // 4. 冲突数据【保留本地的】，因为大部分是珍贵的轨迹收集数据，所以本地为准，服务端数据抛弃
            }
        } catch (e: Exception) {
            //
        }
    }

    suspend fun syncDataPush() {
        val traceRecordList = localDataSource.getUnSyncedTraceRecord().data ?: return
        LogUtils.i("getUnSyncedTraceRecord ${traceRecordList.size}")
        traceRecordList.forEach { add2remote(it) }
    }

    suspend fun getPageList(loadMore: Boolean): ResponseEntity<ArrayList<TraceRecord>> {
        val requestPage = if (loadMore) (curPage + 1) else 1

        // 从本地取
        val response = localDataSource.getPageList(page = requestPage)

        if (response.isSuccess()) {
            val data = response.getSuccessData()
            curPage = requestPage
            if (!loadMore) pageList.clear()
            pageList.addAll(data.records)
        } else {
            pageList.clear()
        }
        return ResponseEntity(pageList, response.code, response.msg)
    }

    suspend fun add(data: TraceRecord): ResponseEntity<TraceRecord> {
        return localDataSource.add(data)
    }

    suspend fun add2remote(dbId: Long): ResponseEntity<TraceRecord> {
        val response = localDataSource.getTraceRecordByDbId(dbId)
        return if (response.isSuccess()) {
            add2remote(response.getSuccessData())
        } else {
            response
        }
    }

    suspend fun add2remote(data: TraceRecord): ResponseEntity<TraceRecord> {
        // 更新本地数据库的id
        val response = remoteDataSource.add(data)
        if (response.isSuccess() && response.data != null) {
            localDataSource.update(response.data)
            // 本地数据更新成功后，记录全局时间戳
            SyncUtils.update(response.data.syncTimestamp)
            LogUtils.i("add success: ${response.data.name} , local update = ${response.data.dbId}")
        }
        return response
    }

    suspend fun update(data: TraceRecord) = service.updateTraceRecord(data.id!!, data)

    suspend fun delete(data: TraceRecord) = localDataSource.delete(data)

}