package com.boredream.lovebook.data.repo

import com.amap.api.location.AMapLocation
import java.util.*
import kotlin.collections.ArrayList

abstract class LocationRepository {

    companion object {

        /**
         * 最小计算追踪点，单位米
         */
        const val TRACE_DISTANCE_THRESHOLD = 1

    }
    var myLocation: AMapLocation? = null
    var onLocationListener: (location: AMapLocation) -> Unit = { }
    var isTracing = false
    val trancePointList = ArrayList<AMapLocation>()
    var onTraceListener: (trancePointList: ArrayList<AMapLocation>) -> Unit = { }

    /**
     * 开始定位
     */
    abstract fun startLocation()

}