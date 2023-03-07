package com.boredream.lovebook.data.repo

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.workDataOf
import com.amap.api.mapcore.util.it
import com.boredream.lovebook.base.BaseRequestRepository
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.repo.source.TraceRecordLocalDataSource
import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.utils.SyncUtils
import com.boredream.lovebook.work.UploadWorker
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 轨迹记录，针对的是整条轨迹线路
 */
@Singleton
class TraceRecordRepository @Inject constructor(
    private val service: ApiService,
    private val localDataSource: TraceRecordLocalDataSource,
    private val workManager: WorkManager
) : BaseRequestRepository<TraceRecord>() {

    /**
     * 同步数据，先拉取，再上传
     */
    suspend fun syncData() {
        syncDataPull()
        syncDataPush()
    }

    private suspend fun syncDataPull() {
        // 服务端把本地时间戳之后的所有数据都查询出来，一次性返回给前端
        val localTimestamp = SyncUtils.get() // TODO: SyncUtils 里有context引用

        // 不关注 response
        val traceRecordList = service.syncTraceRecordList(localTimestamp).data ?: return

        for (traceRecord in traceRecordList) {
            // 1. 拉取的数据先和本地进行比较，id不存在的新数据直接插入，如果是id存在的继续判断
            val response = localDataSource.getTraceRecordById(traceRecord.id!!)
            if(response.data == null) {
                localDataSource.addTraceRecord(traceRecord)
                continue
            }

            // 2. 如果本地待更新的这笔数据，同步标志位是false，直接以服务端为准，覆盖即可
            val localRecord = response.data
            if(!localRecord.synced) {
                localDataSource.addTraceRecord(traceRecord)
                continue
            }

            // 3. 如果本地待更新的这笔数据，同步标志位是true，即本地有修改还没提交给服务端的，进入到冲突处理
            // 4. 冲突数据【保留本地的】，因为大部分是珍贵的轨迹收集数据，所以本地为准，服务端数据抛弃
        }
    }

    private suspend fun syncDataPush() {
        val traceRecordList = localDataSource.getUnSyncedTraceRecord().data ?: return

        // check本地是否有数据要提交
        traceRecordList.filter { !it.synced }
            .forEach { uploadLocal2Remote(it) }
    }

    suspend fun getPageList(loadMore: Boolean, forceRemote: Boolean = false) =
        getPageList(forceRemote, loadMore = loadMore) {
            localDataSource.getTraceRecordList(it)
        }

    suspend fun addLocal(data: TraceRecord): ResponseEntity<Boolean> {
        val response = localDataSource.addTraceRecord(data)
        uploadLocal2Remote(data)
        return commit { response }
    }

    private fun uploadLocal2Remote(data: TraceRecord) {
        // 同时WorkManager同步到服务端
        val uploadWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setInputData(workDataOf("traceRecordDbId" to data.dbId))
            .build()
        workManager.enqueue(uploadWorkRequest)
    }

//    suspend fun add(data: TraceRecord): ResponseEntity<Boolean> {
//        // 提交数据时，放在 traceListStr里，减少报文大小
//        val sb = StringBuilder()
//        data.traceList.forEach {
//            sb.append("_").append(it.time)
//                .append(",").append(it.latitude)
//                .append(",").append(it.longitude)
//        }
//        if(sb.isNotEmpty()) {
//            data.traceListStr = sb.substring(1)
//        }
//        data.traceList.clear()
//        return commit { service.addTraceRecord(data) }
//    }

    suspend fun update(data: TraceRecord) = commit { service.updateTraceRecord(data.id!!, data) }
    suspend fun delete(data: TraceRecord) = commit { localDataSource.deleteTraceRecord(data) }

}