package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseRequestRepository
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.repo.source.TraceRecordLocalDataSource
import com.boredream.lovebook.net.ApiService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 轨迹记录，针对的是整条轨迹线路
 */
@Singleton
class TraceRecordRepository @Inject constructor(
    private val service: ApiService,
    private val localDataSource: TraceRecordLocalDataSource,
) : BaseRequestRepository<TraceRecord>() {

    /**
     * 当前正在本地记录中的轨迹
     */
    private var localTraceRecord: TraceRecord? = null

    /**
     * 开启一条本地记录轨迹
     */
    fun startLocalTraceRecord() {
        localTraceRecord = localDataSource.createTraceRecord()
    }

    /**
     * 向本地记录轨迹插入一条位置信息
     */
    fun appendLocalTraceLocation(traceLocation: TraceLocation) {
        localTraceRecord?.let {
            traceLocation.traceRecordId = it.id
            localDataSource.addTraceLocation(traceLocation)
        }
    }

    suspend fun getPageList(loadMore: Boolean, forceRemote: Boolean = false) =
        getPageList(forceRemote, loadMore = loadMore) {
            service.getTraceRecordList(it)
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
    suspend fun delete(data: TraceRecord) = commit { service.deleteTraceRecord(data.id!!) }

}