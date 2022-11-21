package com.boredream.lovebook.data.repo.source

import android.content.Context
import android.util.Log
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.boredream.lovebook.data.TraceLocation
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GdLocationDataSource @Inject constructor(@ApplicationContext val context: Context) :
    LocationDataSource {

    companion object {
        const val TAG = "GdLocationDataSource"
    }

    override fun startLocation(onSuccess: (location: TraceLocation) -> Unit) {
        // TODO: on error
        try {
            val locationClient = AMapLocationClient(context)
            // 初始化定位参数
            val locationOption = AMapLocationClientOption()
            // 设置定位监听
            locationClient.setLocationListener {
                if (it.errorCode == 0) {
                    onSuccess.invoke(
                        TraceLocation(latitude = it.latitude, longitude = it.longitude)
                    )
                } else Log.e(
                    TAG, "Error, ErrCode:" + it.errorCode + ", errInfo:" + it.errorInfo
                )
            }
            // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            locationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            // 设置定位间隔,单位毫秒,默认为2000ms
            locationOption.interval = 2000
            //设置定位参数
            locationClient.setLocationOption(locationOption)
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            // 启动定位
            locationClient.startLocation()
        } catch (e: Exception) {
            Log.e(TAG, "Exception:$e")
        }
    }

    override fun stopLocation() {

    }

}