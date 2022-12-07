package com.boredream.lovebook.data.usecase

import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.dto.ListResult
import com.boredream.lovebook.data.repo.LocationRepository
import com.boredream.lovebook.data.repo.TraceRecordRepository
import com.boredream.lovebook.utils.TraceUtils
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * TODO: 职责划分？
 * TODO: 不需要单例？
 * 和 repo 的区别，repo 处理数据，use case 处理业务逻辑。
 * 简单的业务，repo = use case，复杂的 use case 一般整合多个 repo。
 */
@Singleton
class TraceUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
    private val traceRecordRepository: TraceRecordRepository,
) {

    companion object {
        const val STATUS_IDLE = 0
        const val STATUS_LOCATION = 1
        const val STATUS_TRACE = 2
    }

    var status = STATUS_IDLE
        set(value) {
            field = value
            onStatusChange.forEach { it.invoke(value) }
        }

    fun getMyLocation() = locationRepository.myLocation

    fun isTracing() = locationRepository.isTracing

    /**
     * 开始定位
     */
    fun startLocation() {
        if(status == STATUS_LOCATION) return
        locationRepository.startLocation()
        status = STATUS_LOCATION
    }

    /**
     * 结束定位
     */
    fun stopLocation() {
        locationRepository.stopLocation()
        // 追踪依赖定位
        stopTrace()
        status = STATUS_IDLE
    }

    /**
     * 开始追踪轨迹
     */
    fun startTrace() {
        // 必须要先开始定位，且会清楚已有轨迹
        locationRepository.clearTraceList()
        locationRepository.startTrace()
        status = STATUS_TRACE
    }

    /**
     * 结束追踪轨迹
     */
    fun stopTrace() {
        locationRepository.stopTrace()
        status = STATUS_LOCATION
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
     * 获取所有历史轨迹
     */
    suspend fun getAllHistoryTraceListRecord(): ResponseEntity<ListResult<TraceRecord>> {
        return traceRecordRepository.getList()
    }

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

    private var onStatusChange: LinkedList<(status: Int) -> Unit> = LinkedList()
    fun addStatusChangeListener(listener: (status: Int) -> Unit) {
        onStatusChange.add(listener)
    }
    fun removeStatusChangeListener(listener: (status: Int) -> Unit) {
        onStatusChange.remove(listener)
    }

}