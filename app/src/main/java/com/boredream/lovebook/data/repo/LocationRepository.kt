package com.boredream.lovebook.data.repo

import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.repo.source.LocationDataSource
import javax.inject.Inject
import javax.inject.Singleton

// 非接口类的 repo
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

    lateinit var traceList: ArrayList<TraceLocation>
    var onTraceSuccess: (tracePointList: ArrayList<TraceLocation>) -> Unit = { }

    /**
     * 开始定位
     */
    fun startLocation() {
        traceList = ArrayList()
        dataSource.startLocation(::onLocationSuccess)
    }

    /**
     * 停止定位
     */
    fun stopLocation() {
        dataSource.stopLocation()
        saveTraceList()
    }

    /**
     * 记录轨迹信息
     */
    fun saveTraceList() {
        // TODO: 记录追踪地址，因为repo不能用context，所以数据本地保存要交给Data source
        dataSource.saveTraceList(traceList)
    }

    fun loadTraceList(traceList: ArrayList<TraceLocation>) {
        this.traceList = traceList
    }

    /**
     * 定位成功
     */
    private fun onLocationSuccess(location: TraceLocation) {
        println("onLocationSuccess dataSource = ${dataSource.javaClass}, location = $location")
        myLocation = location
        onLocationSuccess.invoke(location)
        appendTracePoint(location)
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