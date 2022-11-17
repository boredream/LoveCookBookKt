package com.boredream.lovebook.view

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.*
import com.blankj.utilcode.util.ColorUtils
import com.boredream.lovebook.R


class TraceMapView : MapView {

    private var aMap: AMap = map
    private var zoomLevel = 17f
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

    fun drawTraceStep(lastLocation: AMapLocation, newLocation:AMapLocation) {
        val lastLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)
        val newLatLng = LatLng(newLocation.latitude, newLocation.longitude)
        aMap.addPolyline(PolylineOptions()
                .add(lastLatLng).add(newLatLng)
                .width(20f).color(ContextCompat.getColor(context, R.color.colorPrimary)))
    }

}