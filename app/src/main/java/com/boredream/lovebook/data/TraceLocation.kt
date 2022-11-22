package com.boredream.lovebook.data

import com.blankj.utilcode.util.TimeUtils

data class TraceLocation(
    var time: Long = System.currentTimeMillis(),
    var latitude: Double,
    var longitude: Double,
) {
    override fun toString(): String {
        return "${TimeUtils.millis2String(time)}  $latitude,$longitude"
    }
}