package com.boredream.lovebook.view

import android.content.Context
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.amap.api.mapcore.util.it
import com.amap.api.maps.AMap.OnCameraChangeListener
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
    private var myLocation: TraceLocation? = null
    private var myLocationMarker: Marker? = null
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
            it.uiSettings.isZoomControlsEnabled = false
            it.addOnCameraChangeListener(object : OnCameraChangeListener {
                override fun onCameraChange(position: CameraPosition?) {

                }

                override fun onCameraChangeFinish(position: CameraPosition?) {
                    position?.let { it -> zoomLevel = it.zoom }
                }

            })
            myLocationMarker = it.addMarker(MarkerOptions())
        }
    }

    private var firstMoveCamera = true
    private fun moveCamera(location: TraceLocation) {
//        if(firstMoveCamera) {
//            map.moveCamera(CameraUpdateFactory.zoomTo(zoomLevel))
//            firstMoveCamera = false
//        }

        val position = CameraPosition.Builder()
            .target(LatLng(location.latitude, location.longitude))
            .zoom(zoomLevel)
            .build()
        map.moveCamera(CameraUpdateFactory.newCameraPosition(position))
    }

    fun setMyLocation(location: TraceLocation) {
        myLocation = location
        myLocationMarker?.position = LatLng(location.latitude, location.longitude)

        // TODO: draw my location 和 move camera 分开
        if (isFollowingMode) {
            moveCamera(location)
        }
    }

    /**
     * 绘制正在跟踪的轨迹线路
     */
    fun drawTraceList(allTracePointList: ArrayList<TraceLocation>) {
        if (startDrawIndex >= allTracePointList.size) {
            // 如果开始绘制的位置，超过了轨迹列表大小，代表错误数据or轨迹列表更换了，此次为无效绘制
            return
        }

        val pointList = ArrayList<LatLng>()
        // 从 startDrawIndex 开始绘制
        for (i in startDrawIndex until allTracePointList.size) {
            pointList.add(allTracePointList[i].toLatLng())
        }
        val line = drawLine(pointList)
        if (line != null) {
            // 绘制完成后，更新 startDrawIndex
            val newIndex = allTracePointList.lastIndex
            println("drawTraceList $startDrawIndex to $newIndex")
            startDrawIndex = newIndex
        }
    }

    /**
     * 绘制多条不会变化的线路
     */
    fun drawMultiFixTraceList(multiTracePointList: ArrayList<ArrayList<TraceLocation>>) {
        multiTracePointList.forEach { it ->
            val pointList = ArrayList<LatLng>()
            it.forEach { pointList.add(it.toLatLng()) }
            drawLine(pointList, traceLineColor = ContextCompat.getColor(context, R.color.colorPrimaryLight))
        }
    }

    private fun drawLine(
        pointList: ArrayList<LatLng>,
        traceLineWidth: Float = 15f,
        traceLineColor: Int = ContextCompat.getColor(context, R.color.colorPrimary)
    ): Polyline? {
        // TODO: 中途有多个点定位失败，然后走出很远距离后，再次定位成功（如坐地铁），应该分多条线绘制
        return map.addPolyline(
            PolylineOptions().addAll(pointList).width(traceLineWidth).color(traceLineColor)
        )
    }

    private fun TraceLocation.toLatLng(): LatLng {
        return LatLng(this.latitude, this.longitude)
    }
}