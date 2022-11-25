package com.boredream.lovebook.utils

import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.data.TraceLocation

object TraceUtils {

    fun getTraceListName(traceList: ArrayList<TraceLocation>): String {
        val time = TimeUtils.millis2String(traceList[traceList.lastIndex].time)
        return "轨迹 $time"
    }

    fun calculateDistance(traceList: java.util.ArrayList<TraceLocation>): Float {
        var totalDistance = 0f
        for (i in 1 until traceList.size) {
            val distance = AMapUtils.calculateLineDistance(
                LatLng(traceList[i - 1].latitude, traceList[i - 1].longitude),
                LatLng(traceList[i].latitude, traceList[i].longitude)
            )
            totalDistance += distance
        }
        return totalDistance
    }

}