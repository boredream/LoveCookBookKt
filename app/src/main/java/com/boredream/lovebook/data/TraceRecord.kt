package com.boredream.lovebook.data

/**
 * 轨迹记录
 */
data class TraceRecord(
    val traceList: ArrayList<TraceLocation>,
    var name: String,
    var startTime: Long,
    var endTime: Long,
    var distance: Int, // 单位米
    var detail: String? = null,
    var traceListStr: String? = null,
) : Belong2UserEntity()