package com.boredream.lovebook.data.repo

import com.amap.api.location.AMapLocation
import com.boredream.lovebook.listener.OnCall
import java.util.*

abstract class LocationRepository : OnCall<AMapLocation> {

    companion object {

        /**
         * 最小计算追踪点，单位米
         */
        const val TRACE_DISTANCE_THRESHOLD = 1

    }

    var isTracing = false
    val trancePointList = LinkedList<AMapLocation>()
    var onTranceChangeListener: OnCall<LinkedList<AMapLocation>>? = null

    var myLocation: AMapLocation? = null
    var onLocationSuccessListener: OnCall<AMapLocation>? = null

    /**
     * 开始定位
     */
    abstract fun startLocation()

    /**
     * 添加路径点
     */
    abstract fun appendTracePoint(location: AMapLocation)

}