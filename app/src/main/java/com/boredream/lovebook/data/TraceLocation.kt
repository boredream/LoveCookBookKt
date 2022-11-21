package com.boredream.lovebook.data

data class TraceLocation(
    var time: Long = System.currentTimeMillis(),
    var latitude: Double,
    var longitude: Double,
) {
    override fun toString(): String {
        return "time=$time, latitude=$latitude, longitude=$longitude"
    }
}