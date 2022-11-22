package com.boredream.lovebook.data.repo

import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.repo.source.LocationDataSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 定位数据。这里的追踪数据，只针对定位SDK返回的单次追踪信息
 */
@Singleton
class LocationRepository @Inject constructor(
    private val dataSource: LocationDataSource
) {

    companion object {

        /**
         * 最小计算追踪点，单位米
         */
        const val TRACE_DISTANCE_THRESHOLD = 0.5

    }

    var myLocation: TraceLocation? = null
    var onLocationSuccess: (location: TraceLocation) -> Unit = { }

    var isTracing = false
    var traceList: ArrayList<TraceLocation> = ArrayList()
    var onTraceSuccess: (tracePointList: ArrayList<TraceLocation>) -> Unit = { }

    /**
     * 开始定位
     */
    fun startLocation() {
        dataSource.startLocation(::onLocationSuccess)
    }

    /**
     * 停止定位
     */
    fun stopLocation() {
        dataSource.stopLocation()
    }

    /**
     * 开始追踪
     */
    fun startTrace() {
        traceList.clear()
        isTracing = true
    }

    /**
     * 停止追踪
     */
    fun stopTrace() {
        isTracing = false
    }

    /**
     * 定位成功
     */
    private fun onLocationSuccess(location: TraceLocation) {
        println("onLocationSuccess dataSource = ${dataSource.javaClass.simpleName}, location = $location")
        myLocation = location
        onLocationSuccess.invoke(location)
        if(isTracing) {
            appendTracePoint(location)
        }
    }

    /**
     * 添加定位轨迹追踪点
     */
    private fun appendTracePoint(location: TraceLocation) {
        // 计算新的point和上一个定位point距离
        val lastPointLat = if (traceList.size == 0) 0.0 else traceList[0].latitude
        val lastPointLng = if (traceList.size == 0) 0.0 else traceList[0].longitude
        val distance = AMapUtils.calculateLineDistance(
            LatLng(lastPointLat, lastPointLng),
            LatLng(location.latitude, location.longitude)
        )
        if (distance > TRACE_DISTANCE_THRESHOLD) {
            // 移动距离统计阈值
            traceList.add(location)
            onTraceSuccess.invoke(traceList)
        } else {
            // TODO: 更好的处理
            // 距离达不到阈值时，视为原地不动，只更新最新一次时间？
            traceList[traceList.lastIndex].time = location.time
        }
    }

}