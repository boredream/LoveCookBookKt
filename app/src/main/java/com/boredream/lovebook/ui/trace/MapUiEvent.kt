package com.boredream.lovebook.ui.trace

import com.amap.api.location.AMapLocation

sealed class MapUiEvent
data class MoveToLocation(val location: AMapLocation) : MapUiEvent()
data class DrawMyLocation(val location: AMapLocation) : MapUiEvent()
data class DrawTraceLine(val locationList: ArrayList<AMapLocation>) : MapUiEvent()

