package com.boredream.lovebook.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.*
import com.boredream.lovebook.R
import com.boredream.lovebook.data.TraceLocation

/**
 * 追踪路线的地图
 */
class TraceMapView : MapView {

    private var aMap: AMap = map
    private var zoomLevel = 17f
    private var traceLineWidth = 15f
    private var traceLineColor = ContextCompat.getColor(context, R.color.colorPrimary)
    private var myLocationMarker: Marker
    private lateinit var mapOverlay: Polygon
    private val holeOptionsList : MutableList<BaseHoleOptions> = ArrayList()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        // TODO: map 和 location 绑定？

        // 样式
        aMap.uiSettings.isScaleControlsEnabled = false
        aMap.uiSettings.isZoomControlsEnabled = true
        myLocationMarker = aMap.addMarker(MarkerOptions())

        // drawTraceOverlay()
    }

    fun moveCamera(location: TraceLocation) {
        val position = CameraPosition.Builder()
            .target(LatLng(location.latitude, location.longitude))
            .zoom(zoomLevel)
            .build()
        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(position))
    }

    fun drawMyLocation(location: TraceLocation) {
        val latLng = LatLng(location.latitude, location.longitude)
        myLocationMarker.position = latLng
    }

    fun drawTraceList(locationList: ArrayList<TraceLocation>) {
        val pointList = ArrayList<LatLng>()
        locationList.forEach { pointList.add(LatLng(it.latitude, it.longitude)) }
        aMap.addPolyline(
            PolylineOptions().addAll(pointList).width(traceLineWidth).color(traceLineColor)
        )

        // drawTraceOverlayHollow(locationList)
    }

    // 绘制遮罩 https://lbs.amap.com/demo/javascript-api/example/overlayers/cover
    private fun drawTraceOverlay() {
        // 遮罩
        val polygonOptions = PolygonOptions()
        val outer = arrayListOf(
            LatLng(-90.0, -180.0, true),
            LatLng(-90.0, 179.9999, true),
            LatLng(90.0, 179.9999, true),
            LatLng(90.0, -180.0, true),
        )
        polygonOptions
            .addAll(outer)
            .fillColor(Color.argb(90, 0, 0, 0))
            .strokeWidth(0f)
        
        mapOverlay = aMap.addPolygon(polygonOptions)
    }

    private fun drawTraceOverlayHollow(locationList: ArrayList<TraceLocation>) {
        // 遮罩挖孔
        val lastLocation = locationList.last()
        val hollow = arrayListOf(
            LatLng(lastLocation.latitude, lastLocation.longitude),
            LatLng(lastLocation.latitude + 0.001, lastLocation.longitude),
            LatLng(lastLocation.latitude + 0.001, lastLocation.longitude + 0.001),
            LatLng(lastLocation.latitude, lastLocation.longitude + 0.001),
        )
        val holeOptions = PolygonHoleOptions()
        holeOptions.addAll(hollow)
        holeOptionsList.add(holeOptions)

        // 高德地图的规则是，新增的Holy如果和原有的有形状重叠，则跳过不再重复添加
        // TODO: 如何根据线路更好的绘制蒙版+擦除效果？
        mapOverlay.holeOptions = holeOptionsList
    }

}