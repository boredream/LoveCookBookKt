package com.boredream.lovebook.ui.trace

import com.boredream.lovebook.data.TraceLocation

data class TraceMapLocationState(
    val myLocation: TraceLocation? = null,
    val isFollowing: Boolean = true,
)