package com.boredream.lovebook.data

import com.blankj.utilcode.util.TimeUtils

class DrawTraceLocation(
    time: Long = System.currentTimeMillis(),
    latitude: Double,
    longitude: Double,
) : TraceLocation(time, latitude, longitude)