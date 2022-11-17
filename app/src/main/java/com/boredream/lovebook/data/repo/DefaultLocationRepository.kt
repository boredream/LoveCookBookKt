package com.boredream.lovebook.data.repo

import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.boredream.lovebook.data.repo.source.GdLocationDataSource
import javax.inject.Inject
import javax.inject.Singleton

// 非接口类的 repo
@Singleton
class DefaultLocationRepository @Inject constructor(private val dataSource: GdLocationDataSource) :
    LocationRepository() {

    // TODO: 抽象location

    /**
     * 开始定位
     */
    override fun startLocation() {
        dataSource.onLocationSuccessListener = this
        dataSource.startLocation()
    }

    /**
     * 添加路径点
     */
    override fun appendTracePoint(location: AMapLocation) {
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