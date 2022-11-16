package com.boredream.lovebook.data.repo

import com.amap.api.location.AMapLocation
import com.boredream.lovebook.data.repo.source.GdLocationDataSource
import com.boredream.lovebook.listener.OnCall
import javax.inject.Inject
import javax.inject.Singleton

// 非接口类的 repo
@Singleton
class LocationRepository @Inject constructor(private val dataSource: GdLocationDataSource) :
    OnCall<AMapLocation> {

    fun startLocation() {
        dataSource.onLocationSuccessListener = this
        dataSource.startLocation()
    }

    override fun call(t: AMapLocation) {
        // 定位成功回调
    }

}