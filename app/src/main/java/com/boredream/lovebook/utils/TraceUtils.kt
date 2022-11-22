package com.boredream.lovebook.utils

import com.boredream.lovebook.data.TraceLocation

object TraceUtils {

    fun getTraceListName(traceList: ArrayList<TraceLocation>): String {
        return "trace_$traceList[traceList.lastIndex].time"
    }

}