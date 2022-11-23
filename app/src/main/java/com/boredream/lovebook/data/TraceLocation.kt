package com.boredream.lovebook.data

import com.blankj.utilcode.util.TimeUtils

open class TraceLocation(
    var time: Long = System.currentTimeMillis(),
    var latitude: Double,
    var longitude: Double,
) : java.io.Serializable {
    override fun toString(): String {
        return "${TimeUtils.millis2String(time)}  $latitude,$longitude"
    }
}