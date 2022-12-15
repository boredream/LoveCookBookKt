package com.boredream.lovebook.data.usecase

import com.boredream.lovebook.base.BaseUseCase
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.repo.LocationRepository
import com.boredream.lovebook.data.repo.TraceRecordRepository
import com.boredream.lovebook.utils.TraceUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraceUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val traceRecordRepository: TraceRecordRepository,
): BaseUseCase() {

    fun getMyLocation() = locationRepository.myLocation

    fun isTracing() = locationRepository.status == LocationRepository.STATUS_TRACE

    /**
     * 开始定位
     */
    fun startLocation() {
        locationRepository.startLocation()
    }

    /**
     * 结束定位
     */
    fun stopLocation() {
        locationRepository.stopLocation()
    }

    /**
     * 开始追踪轨迹
     */
    fun startTrace() {
        // 必须要先开始定位，且会清除已有轨迹
        locationRepository.clearTraceList()
        locationRepository.startTrace()
    }

    /**
     * 结束追踪轨迹
     */
    fun stopTrace() {
        locationRepository.stopTrace()
    }

    /**
     * 保存追踪轨迹
     */
    suspend fun saveTraceRecord(): ResponseEntity<Boolean> {
        val traceList = locationRepository.traceList
        val title = TraceUtils.getTraceListName(traceList)
        val startTime = traceList[0].time
        val endTime = traceList[traceList.lastIndex].time
        val distance = TraceUtils.calculateDistance(traceList)
        val traceRecord = TraceRecord(traceList, title, startTime, endTime, distance)
        return traceRecordRepository.add(traceRecord)
    }

    /**
     * 清除当前轨迹
     */
    fun clearTrace() {
        locationRepository.clearTraceList()
    }

    /**
     * TODO: 获取所有历史轨迹 / 只获取附近的
     */
//    suspend fun getAllHistoryTraceListRecord(): ResponseEntity<ListResult<TraceRecord>> {
//        return traceRecordRepository.getList()
//    }

    // TODO: 回调适合用函数吗？
    fun addLocationSuccessListener(listener: (location: TraceLocation) -> Unit) {
        locationRepository.addLocationSuccessListener(listener)
    }
    fun removeLocationSuccessListener(listener: (location: TraceLocation) -> Unit) {
        locationRepository.removeLocationSuccessListener(listener)
    }

    fun addTraceSuccessListener(listener: (allTracePointList: ArrayList<TraceLocation>) -> Unit) {
        locationRepository.addTraceSuccessListener(listener)
    }
    fun removeTraceSuccessListener(listener: (allTracePointList: ArrayList<TraceLocation>) -> Unit) {
        locationRepository.removeTraceSuccessListener(listener)
    }

    fun addStatusChangeListener(listener: (status: Int) -> Unit) {
        locationRepository.addStatusChangeListener(listener)
    }
    fun removeStatusChangeListener(listener: (status: Int) -> Unit) {
        locationRepository.removeStatusChangeListener(listener)
    }

}