package com.boredream.lovebook.data.repo

import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.repo.source.LocationDataSource
import com.boredream.lovebook.utils.TraceFilter
import java.util.*
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

    // 定位
    var myLocation: TraceLocation? = null
    private var onLocationSuccess: LinkedList<(location: TraceLocation) -> Unit> = LinkedList()
    fun addLocationSuccessListener(listener: (location: TraceLocation) -> Unit) {
        onLocationSuccess.add(listener)
    }
    fun removeLocationSuccessListener(listener: (location: TraceLocation) -> Unit) {
        onLocationSuccess.remove(listener)
    }

    // 追踪
    var isTracing = false
    var traceList: ArrayList<TraceLocation> = ArrayList()
    private lateinit var traceFilter: TraceFilter
    private var onTraceSuccess: LinkedList<(allTracePointList: ArrayList<TraceLocation>) -> Unit> = LinkedList()
    fun addTraceSuccessListener(listener: (allTracePointList: ArrayList<TraceLocation>) -> Unit) {
        onTraceSuccess.add(listener)
    }
    fun removeTraceSuccessListener(listener: (allTracePointList: ArrayList<TraceLocation>) -> Unit) {
        onTraceSuccess.remove(listener)
    }

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
        traceFilter = TraceFilter()
        isTracing = true
    }

    /**
     * 停止追踪
     */
    fun stopTrace() {
        isTracing = false
    }

    /**
     * 清除追踪数据
     */
    fun clearTraceList() {
        traceList.clear()
    }

    /**
     * 定位成功
     */
    fun onLocationSuccess(location: TraceLocation) {
        println("onLocationSuccess dataSource = ${dataSource.javaClass.simpleName}, location = $location")
        myLocation = location
        onLocationSuccess.forEach { it.invoke(location) }
        if(isTracing) {
            appendTracePoint(location)
        }
    }

    /**
     * 添加定位轨迹追踪点
     */
    private fun appendTracePoint(location: TraceLocation) {
        // FIXME: 暂时记录原始数据，用于调试
//        if(!traceFilter.filterPos(location)) {
//            return
//        }

        traceList.add(location)
        onTraceSuccess.forEach { it.invoke(traceList) }

//        // 计算新的point和上一个定位point距离
//        val lastPointLat = if (traceList.size == 0) 0.0 else traceList[0].latitude
//        val lastPointLng = if (traceList.size == 0) 0.0 else traceList[0].longitude
//        val distance = AMapUtils.calculateLineDistance(
//            LatLng(lastPointLat, lastPointLng),
//            LatLng(location.latitude, location.longitude)
//        )
//        if (distance > TRACE_DISTANCE_THRESHOLD) {
//            // 移动距离统计阈值
//            traceList.add(location)
//            onTraceSuccess.invoke(traceList)
//        } else {
//            // 距离达不到阈值时，视为原地不动，只更新最新一次时间？
//            traceList[traceList.lastIndex].time = location.time
//        }
    }

}