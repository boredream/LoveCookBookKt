package com.boredream.lovebook.view

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.amap.api.mapcore.util.it
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.*
import com.boredream.lovebook.R
import com.boredream.lovebook.data.TraceLocation

/**
 * 追踪路线的地图
 */
class TraceMapView : MapView {

    private var zoomLevel = 15f
    private var traceLineWidth = 15f
    private var traceLineColor = ContextCompat.getColor(context, R.color.colorPrimary)
    private var myLocation: TraceLocation? = null
    private lateinit var myLocationMarker: Marker
    private var allTracePointList: ArrayList<TraceLocation> = ArrayList()
    private var startDrawIndex = 0

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

    fun drawTraceList(allTracePointList: ArrayList<TraceLocation>) {
        if(startDrawIndex >= allTracePointList.size) {
            // 如果开始绘制的位置，超过了轨迹列表大小，代表错误数据or轨迹列表更换了，此次为无效绘制
            return
        }

        this.allTracePointList = allTracePointList
        val pointList = ArrayList<LatLng>()
        // 从 startDrawIndex 开始绘制
        for(i in startDrawIndex until allTracePointList.size) {
            val point = allTracePointList[i]
            pointList.add(LatLng(point.latitude, point.longitude))
        }
        val line = map.addPolyline(
            PolylineOptions().addAll(pointList).width(traceLineWidth).color(traceLineColor)
        )
        if(line != null) {
            // 绘制完成后，更新 startDrawIndex
            val newIndex = allTracePointList.lastIndex
            println("drawTraceList $startDrawIndex to $newIndex")
            startDrawIndex = newIndex
        }
    }

//    fun drawTraceList(locationList: ArrayList<TraceLocation>) {
//        val pointList = ArrayList<LatLng>()
//        locationList.forEach { pointList.add(LatLng(it.latitude, it.longitude)) }
//        val line = map.addPolyline(
//            PolylineOptions().addAll(pointList).width(traceLineWidth).color(traceLineColor)
//        )
//        println(line)
//    }

}