package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseRequestRepository
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.repo.source.TraceRecordLocalDataSource
import com.boredream.lovebook.net.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraceRecordRepository @Inject constructor(
    private val service: ApiService,
    private val localDataSource: TraceRecordLocalDataSource,
) : BaseRequestRepository<TraceRecord>() {

    // TODO: 轨迹数据量比较大，可以考虑保存在本地然后增量更新

    suspend fun getPageList(loadMore: Boolean, forceRemote: Boolean = false) =
        getPageList(forceRemote, loadMore = loadMore) {
            service.getTraceRecordList(it)
        }

    suspend fun add(data: TraceRecord) = commit { service.addTraceRecord(data) }
    suspend fun update(data: TraceRecord) = commit { service.updateTraceRecord(data.id!!, data) }
    suspend fun delete(data: TraceRecord) = commit { service.deleteTraceRecord(data.id!!) }

}