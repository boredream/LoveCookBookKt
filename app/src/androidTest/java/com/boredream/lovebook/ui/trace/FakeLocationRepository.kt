package com.boredream.lovebook.ui.trace

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.widget.Toast
import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.boredream.lovebook.data.repo.LocationRepository
import javax.inject.Inject


class FakeLocationRepository @Inject constructor() : LocationRepository() {

    private lateinit var countDownTimer: CountDownTimer

    override fun startLocation() {

        // latitude=31.217792#longitude=121.355379#province=上海市#coordType=GCJ02#city=上海市#district=长宁区#cityCode=021#adCode=310105#address=上海市长宁区协和路789号靠近山里亲戚(淞沪路店)#country=中国#road=协和路#poiName=山里亲戚(淞沪路店)#street=协和路#streetNum=789号#aoiName=中山国际广场#poiid=B0FFHRUNOP#floor=#errorCode=0#errorInfo=success#locationDetail=#id:SbDYzNzVjOTM5ZjAwNW9tb2ZqcGlqNjRhZjhiN2Y3LA==#csid:8297593f13e64158828cf8685b294b01#description=在山里亲戚(淞沪路店)附近#locationType=5#conScenario=99
        val startLocation = AMapLocation("test")
        startLocation.latitude = 31.227792
        startLocation.longitude = 121.355379

        // 开启定时任务，然后挨个返回虚拟经纬度
        val total = (10 * 60 * 1000 + 100).toLong()
        countDownTimer = object : CountDownTimer(total, 2000) {
            override fun onTick(millisUntilFinished: Long) {
                val location = AMapLocation("mockLocation$millisUntilFinished")
                location.latitude = startLocation.latitude + 0.0001
                location.longitude = startLocation.latitude
                onLocationSuccess(location)
            }

            override fun onFinish() {

            }
        }
        countDownTimer.start()
    }

    private fun onLocationSuccess(location: AMapLocation) {
        myLocation = location
        if (isTracing) {
            appendTracePoint(location)
        }
        onLocationListener.invoke(location)
    }

    private fun appendTracePoint(location: AMapLocation) {
        // 计算新的point和上一个定位point距离
        val lastPointLat = if (trancePointList.size == 0) 0.0 else trancePointList[0].latitude
        val lastPointLng = if (trancePointList.size == 0) 0.0 else trancePointList[0].longitude

//        val distance = AMapUtils.calculateLineDistance(
//            LatLng(lastPointLat, lastPointLng),
//            LatLng(location.latitude, location.longitude)
//        )
//
//        if (distance > TRACE_DISTANCE_THRESHOLD) {
            trancePointList.add(location)
//        }
        onTraceListener.invoke(trancePointList)
    }

}