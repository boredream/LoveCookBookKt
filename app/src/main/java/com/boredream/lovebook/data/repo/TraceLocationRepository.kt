package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseRequestRepository
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.net.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraceLocationRepository @Inject constructor(
    private val service: ApiService,
) : BaseRequestRepository<TraceLocation>(service) {

    // TODO: 轨迹数据量比较大，可以考虑保存在本地然后增量更新

    suspend fun getList(recordId: String) = getList { service.getTraceLocationList(recordId) }

}