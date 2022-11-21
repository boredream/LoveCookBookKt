package com.boredream.lovebook.data.repo.source

import android.content.Context
import android.util.Log
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.utils.DataStoreUtils
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.util.ArrayList
import javax.inject.Inject

class GdLocationDataSource @Inject constructor(@ApplicationContext val context: Context) :
    LocationDataSource {

    companion object {
        const val TAG = "GdLocationDataSource"
    }

    private var locationClient: AMapLocationClient? = null

    override fun startLocation(onSuccess: (location: TraceLocation) -> Unit) {
        // TODO: on error
        try {
            locationClient = AMapLocationClient(context)
            locationClient?.let {
                initClient(it, onSuccess)
                it.startLocation()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Exception:$e")
        }
    }

    private fun initClient(
        client: AMapLocationClient,
        onSuccess: (location: TraceLocation) -> Unit
    ) {
        // 初始化定位参数
        val locationOption = AMapLocationClientOption()
        // 设置定位监听
        client.setLocationListener {
            if (it.errorCode == 0) {
                onSuccess.invoke(TraceLocation(latitude = it.latitude, longitude = it.longitude))
            } else Log.e(
                TAG, "initClient Error, ErrCode:" + it.errorCode + ", errInfo:" + it.errorInfo
            )
        }
        // 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        locationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        // 设置定位间隔,单位毫秒,默认为2000ms
        locationOption.interval = 2000
        //设置定位参数
        client.setLocationOption(locationOption)
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        // 启动定位
    }

    override fun stopLocation() {
        locationClient?.stopLocation()
    }

    override fun saveTraceList(traceList: ArrayList<TraceLocation>) {
        // TODO: 本地数据库？
        val sb = StringBuilder()
        val title = TimeUtils.getNowString()
        sb.append(" ======== trace list $title ======== ")
        traceList.forEach { sb.append("\n").append(it) }
        val file = File(PathUtils.getInternalAppFilesPath(), "traceList$title.txt")
        FileIOUtils.writeFileFromString(file, sb.toString())
        Log.i(TAG, "saveTraceList $title")
    }

}