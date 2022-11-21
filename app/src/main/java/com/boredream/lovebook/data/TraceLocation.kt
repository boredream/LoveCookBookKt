package com.boredream.lovebook.data

data class TraceLocation(
    var time: Long = System.currentTimeMillis(),
    var latitude: Double,
    var longitude: Double
) {
    constructor() : this(0, 0.0, 0.0)
}