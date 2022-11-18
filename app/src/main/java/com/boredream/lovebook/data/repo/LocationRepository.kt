package com.boredream.lovebook.data.repo

import com.boredream.lovebook.data.TraceLocation

abstract class LocationRepository {

    companion object {

        /**
         * 最小计算追踪点，单位米
         */
        const val TRACE_DISTANCE_THRESHOLD = 1

    }
    var myLocation: TraceLocation? = null
    var onLocationListener: (location: TraceLocation) -> Unit = { }
    var isTracing = false
    val trancePointList = ArrayList<TraceLocation>()
    var onTraceListener: (trancePointList: ArrayList<TraceLocation>) -> Unit = { }

    /**
     * 开始定位
     */
    abstract fun startLocation()

}