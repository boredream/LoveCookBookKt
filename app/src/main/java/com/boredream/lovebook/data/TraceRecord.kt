package com.boredream.lovebook.data

/**
 * 轨迹记录
 */
data class TraceRecord(
    val traceList: ArrayList<TraceLocation>,
    val title: String,
    val startTime: Long,
    val endTime: Long,
    val distance: Float, // 单位米
) : Belong2UserEntity()