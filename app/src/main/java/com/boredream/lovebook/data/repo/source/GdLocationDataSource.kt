package com.boredream.lovebook.data.repo.source

import android.content.Context
import android.util.Log
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.boredream.lovebook.listener.OnCall
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GdLocationDataSource @Inject constructor(@ApplicationContext val context: Context) {

    companion object {
        const val TAG = "GdLocationDataSource"
    }

    var onLocationSuccessListener: OnCall<AMapLocation>? = null

    fun startLocation() {
        try {
            val mlocationClient = AMapLocationClient(context)
            // 初始化定位参数
            val mLocationOption = AMapLocationClientOption()
            // 设置定位监听
            mlocationClient.setLocationListener {
                it?.let {
                    if (it.errorCode == 0) locationSuccess(it)
                    else Log.e(TAG, "Error, ErrCode:" + it.errorCode + ", errInfo:" + it.errorInfo)
                }
            }
            // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
            // 设置定位间隔,单位毫秒,默认为2000ms
            mLocationOption.interval = 2000
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption)
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            // 启动定位
            mlocationClient.startLocation()
        } catch (e: Exception) {
            Log.e(TAG, "Exception:$e")
        }
    }

    private fun locationSuccess(location: AMapLocation) {
        // 定位成功回调信息，设置相关消息
        location.getLocationType()//获取当前定位结果来源，如网络定位结果，详见定位类型表
        location.getLatitude()//获取纬度
        location.getLongitude()//获取经度
        location.getAccuracy()//获取精度信息
        Log.i(TAG, "locationSuccess: $location")
        onLocationSuccessListener?.call(location)
    }

}