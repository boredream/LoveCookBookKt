package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseRequestRepository
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.net.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraceLocationRepository @Inject constructor(
    private val service: ApiService,
) : BaseRequestRepository<TraceLocation>() {

    // TODO: 轨迹数据量比较大，可以考虑保存在本地然后增量更新

    private var traceRecordId: String? = null
    fun init(traceRecordId: String) {
        this.traceRecordId?.let {
            if (it != traceRecordId) {
                // 当切换轨迹记录时，清空缓存
                cacheIsDirty = true
            }
        }
        this.traceRecordId = traceRecordId
    }

    suspend fun getList() = getList { service.getTraceLocationList(traceRecordId!!) }

}