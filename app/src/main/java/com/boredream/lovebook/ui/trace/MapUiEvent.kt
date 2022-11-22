package com.boredream.lovebook.ui.trace

import com.boredream.lovebook.data.TraceLocation


sealed class MapUiEvent
data class MoveToLocation(val location: TraceLocation) : MapUiEvent()
data class SuccessLocation(val location: TraceLocation) : MapUiEvent()
data class DrawTraceLine(val locationList: ArrayList<TraceLocation>) : MapUiEvent()

