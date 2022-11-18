package com.boredream.lovebook.view

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.*
import com.boredream.lovebook.R

/**
 * 追踪路线的地图
 */
class TraceMapView : MapView {

    private var aMap: AMap = map
    private var zoomLevel = 17f
    private var traceLineWidth = 15f
    private var traceLineColor = ContextCompat.getColor(context, R.color.colorPrimary)
    private var myLocationMarker: Marker

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        // 样式
        aMap.uiSettings.isScaleControlsEnabled = false
        aMap.uiSettings.isZoomControlsEnabled = false
        myLocationMarker = aMap.addMarker(MarkerOptions())

//        val location: ShLocationInfo = LocationKeeper.getSingleton().getLocation()
//        if (location != null) {
//            // 如果外层定位过一次，这里直接默认地图跳转到当前位置
//            moveCamera(location.getLatitude(), location.getLongitude())
//        }

        // TODO: map 和 location 绑定？
    }

    fun moveCamera(location: AMapLocation) {
        val position = CameraPosition.Builder()
            .target(LatLng(location.latitude, location.longitude))
            .zoom(zoomLevel)
            .build()
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(position))
    }

    fun drawMyLocation(location: AMapLocation) {
        val latLng = LatLng(location.latitude, location.longitude)
        myLocationMarker.position = latLng
    }

    fun drawTraceStep(lastLocation: AMapLocation, newLocation: AMapLocation) {
        val locationList = ArrayList<AMapLocation>()
        locationList.add(lastLocation)
        locationList.add(newLocation)
        drawTraceList(locationList)
    }

    fun drawTraceList(locationList: ArrayList<AMapLocation>) {
        val pointList = ArrayList<LatLng>()
        locationList.forEach { pointList.add(LatLng(it.latitude, it.longitude)) }
        aMap.addPolyline(
            PolylineOptions().addAll(pointList).width(traceLineWidth).color(traceLineColor)
        )
    }

}