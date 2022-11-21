package com.boredream.lovebook.data.repo.source

import com.boredream.lovebook.data.TraceLocation
import java.util.ArrayList

interface LocationDataSource {

    /**
     * 开始定位
     */
    fun startLocation(onSuccess: (location: TraceLocation) -> Unit)

    /**
     * 停止定位
     */
    fun stopLocation()

    /**
     * 保存轨迹列表
     */
    fun saveTraceList(traceList: ArrayList<TraceLocation>)

}