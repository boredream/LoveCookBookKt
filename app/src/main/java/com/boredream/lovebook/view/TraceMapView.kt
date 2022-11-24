package com.boredream.lovebook.view

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.*
import com.boredream.lovebook.R
import com.boredream.lovebook.data.TraceLocation

/**
 * 追踪路线的地图
 */
class TraceMapView : MapView {

    private var zoomLevel = 17f
    private var traceLineWidth = 15f
    private var traceLineColor = ContextCompat.getColor(context, R.color.colorPrimary)
    private var myLocation: TraceLocation? = null
    private lateinit var myLocationMarker: Marker

    var isFollowingMode = true
        set(value) {
            field = value
            // set true 时，先移动一次camera
            if (value) myLocation?.let { moveCamera(it) }
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        // 样式
        map?.let {
            it.uiSettings.isScaleControlsEnabled = false
            it.uiSettings.isZoomControlsEnabled = true
            myLocationMarker = it.addMarker(MarkerOptions())
        }
    }

    fun moveCamera(location: TraceLocation) {
        val position = CameraPosition.Builder()
            .target(LatLng(location.latitude, location.longitude))
            .zoom(zoomLevel)
            .build()
        map.moveCamera(CameraUpdateFactory.newCameraPosition(position))
    }

    fun setMyLocation(location: TraceLocation) {
        myLocation = location
        myLocationMarker.position = LatLng(location.latitude, location.longitude)

        if (isFollowingMode) {
            moveCamera(location)
        }
    }

    fun drawTraceList(locationList: ArrayList<TraceLocation>) {
        val pointList = ArrayList<LatLng>()
        locationList.forEach { pointList.add(LatLng(it.latitude, it.longitude)) }
        val line = map.addPolyline(
            PolylineOptions().addAll(pointList).width(traceLineWidth).color(traceLineColor)
        )
        println(line)
    }

}