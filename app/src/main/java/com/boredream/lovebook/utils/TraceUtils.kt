package com.boredream.lovebook.utils

import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.data.TraceLocation

object TraceUtils {

    fun getTraceListName(traceList: ArrayList<TraceLocation>): String {
        val time = TimeUtils.millis2String(traceList[traceList.lastIndex].time)
        return "trace_$time"
    }

}