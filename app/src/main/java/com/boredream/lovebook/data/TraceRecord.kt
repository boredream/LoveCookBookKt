package com.boredream.lovebook.data

import com.boredream.lovebook.utils.TraceUtils

/**
 * 轨迹记录
 */
data class TraceRecord(
    val traceList: ArrayList<TraceLocation>,
    val title: String = TraceUtils.getTraceListName(traceList),
) : Belong2UserEntity()