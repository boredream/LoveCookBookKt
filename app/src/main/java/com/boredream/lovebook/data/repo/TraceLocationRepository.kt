package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseRequestRepository
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.repo.source.TraceRecordLocalDataSource
import com.boredream.lovebook.net.ApiService
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 轨迹记录，针对的是整条轨迹线路
 */
@Singleton
class TraceLocationRepository @Inject constructor(
    private val service: ApiService,
    private val localDataSource: TraceRecordLocalDataSource,
) : BaseRequestRepository<TraceLocation>() {

    suspend fun getLocalTraceLocationList(dbId: Long) =
        getList {
            // service.getTraceRecordList(it)
            localDataSource.getTraceLocationList(dbId)
        }

}