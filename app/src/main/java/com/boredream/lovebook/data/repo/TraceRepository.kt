package com.boredream.lovebook.data.repo

import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.repo.LocationRepository.Companion.TRACE_DISTANCE_THRESHOLD
import com.boredream.lovebook.data.repo.source.TraceDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraceRepository @Inject constructor(
    private val dataSource: TraceDataSource
) {

    lateinit var traceList: ArrayList<TraceLocation>
    var onTraceSuccess: (tracePointList: ArrayList<TraceLocation>) -> Unit = { }

    /**
     * 开始追踪
     */
    fun startNewTraceList() {
        traceList = ArrayList()
    }

    /**
     * 记录轨迹信息
     */
    fun saveTraceList() {
        // TODO: 记录追踪地址，因为repo不能用context，所以数据本地保存要交给Data source
        dataSource.saveTraceList(traceList)
    }

    fun loadTraceList(traceListTitle: String) {
        this.traceList = dataSource.loadTraceList(traceListTitle)
    }

    /**
     * 添加定位轨迹追踪点
     */
    fun appendTracePoint(location: TraceLocation) {
        // 计算新的point和上一个定位point距离
        val lastPointLat = if (traceList.size == 0) 0.0 else traceList[0].latitude
        val lastPointLng = if (traceList.size == 0) 0.0 else traceList[0].longitude
        // 经纬度差值 0.00001 ~= 1米+
        val distance = AMapUtils.calculateLineDistance(
            LatLng(lastPointLat, lastPointLng),
            LatLng(location.latitude, location.longitude)
        )
        println("appendTracePoint distance = $distance")
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