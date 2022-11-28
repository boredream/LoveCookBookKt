package com.boredream.lovebook.data

import com.blankj.utilcode.util.TimeUtils

open class TraceLocation(
    var time: Long = System.currentTimeMillis(),
    var latitude: Double,
    var longitude: Double,
    var extraData: String? = null,
) : java.io.Serializable {
    override fun toString(): String {
        return "${TimeUtils.millis2String(time)}  $latitude,$longitude"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as TraceLocation
        if (latitude != other.latitude) return false
        if (longitude != other.longitude) return false
        return true
    }

    override fun hashCode(): Int {
        var result = latitude.hashCode()
        result = 31 * result + longitude.hashCode()
        return result
    }

}