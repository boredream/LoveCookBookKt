package com.boredream.lovebook.data.repo

import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.boredream.lovebook.data.repo.source.GdLocationDataSource
import com.boredream.lovebook.listener.OnCall
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

// 非接口类的 repo
@Singleton
class LocationRepository @Inject constructor(private val dataSource: GdLocationDataSource) :
    OnCall<AMapLocation> {

    // TODO: 抽象location

    companion object {

        /**
         * 最小计算追踪点，单位米
         */
        const val TRACE_DISTANCE_THRESHOLD = 1

    }

    var isTracing = false
    val trancePointList = LinkedList<AMapLocation>()
    var onTranceChangeListener: OnCall<LinkedList<AMapLocation>>? = null

    var myLocation: AMapLocation? = null
    var onLocationSuccessListener: OnCall<AMapLocation>? = null

    /**
     * 开始定位
     */
    fun startLocation() {
        dataSource.onLocationSuccessListener = this
        dataSource.startLocation()
    }

    /**
     * 开始追踪路径
     */
    fun startTrace() {
        isTracing = true
    }

    /**
     * 添加路径点
     */
    private fun appendTracePoint(location: AMapLocation) {
        // 计算新的point和上一个定位point距离
        val lastPointLat = if (trancePointList.size == 0) 0.0 else trancePointList[0].latitude
        val lastPointLng = if (trancePointList.size == 0) 0.0 else trancePointList[0].longitude

        val distance = AMapUtils.calculateLineDistance(
            LatLng(lastPointLat, lastPointLng),
            LatLng(location.latitude, location.longitude)
        )

        if (distance > TRACE_DISTANCE_THRESHOLD) {
            trancePointList.add(location)
            onTranceChangeListener?.call(trancePointList)
        }

    }

    /**
     * 定位成功回调
     */
    override fun call(t: AMapLocation) {
        myLocation = t
        onLocationSuccessListener?.call(t)

        if (isTracing) {
            appendTracePoint(t)
        }
    }

}