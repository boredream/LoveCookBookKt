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

    suspend fun getPageList(loadMore: Boolean, forceRemote: Boolean = false) =
        getPageList(forceRemote, loadMore = loadMore) {
            localDataSource.getTraceRecordList(it)
        }

    suspend fun addLocal(data: TraceRecord): ResponseEntity<Boolean> {
        val response = localDataSource.addTraceRecord(data)

        // 同时WorkManager同步到服务端
        val uploadWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setInputData(workDataOf("traceRecordDbId" to data.dbId))
            .build()
        workManager.enqueue(uploadWorkRequest)

        return commit { response }
    }

    suspend fun add(data: TraceRecord): ResponseEntity<Boolean> {
        // 提交数据时，放在 traceListStr里，减少报文大小
        val sb = StringBuilder()
        data.traceList.forEach {
            sb.append("_").append(it.time)
                .append(",").append(it.latitude)
                .append(",").append(it.longitude)
        }
        if(sb.isNotEmpty()) {
            data.traceListStr = sb.substring(1)
        }
        data.traceList.clear()
        return commit { service.addTraceRecord(data) }
    }

    suspend fun update(data: TraceRecord) = commit { service.updateTraceRecord(data.id!!, data) }
    suspend fun delete(data: TraceRecord) = commit { localDataSource.deleteTraceRecord(data) }

}