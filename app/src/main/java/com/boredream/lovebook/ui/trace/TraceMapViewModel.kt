package com.boredream.lovebook.ui.trace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.CollectionUtils
import com.boredream.lovebook.base.BaseRequestViewModel
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.usecase.TraceUseCase
import com.boredream.lovebook.vm.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TraceMapViewModel @Inject constructor(
    private val traceUseCase: TraceUseCase
) : BaseRequestViewModel<TraceRecord>() {

    // TODO: 如何更好的设计地图这种 命令式传统view 和 vm 的关系？

    private var isPause = false
    private var pauseCacheDrawTraceList = ArrayList<TraceLocation>()

    // 地图事件
    private val _mapEvent = SingleLiveEvent<MapUiEvent>()
    val mapEvent: LiveData<MapUiEvent> = _mapEvent

    // 单独更新的需要各自LiveData

    // 主UI元素
    private val _uiState = MutableLiveData(TraceMapUiState())
    val uiState: LiveData<TraceMapUiState> = _uiState

    // 是否为跟踪模式
    private val _isFollowing = MutableLiveData(true)
    val isFollowing: LiveData<Boolean> = _isFollowing

    // 是否正在记录轨迹中
    private val _isTracing = MutableLiveData(false)
    val isTracing: LiveData<Boolean> = _isTracing

    /**
     * 页面开始时，打开定位
     */
    fun start() {
        traceUseCase.setOnLocationSuccess { onLocationSuccess(it) }
        traceUseCase.startLocation()
    }

    /**
     * 页面结束时，关闭定位
     */
    fun stop() {
        traceUseCase.stopLocation()
    }

    /**
     * 切换轨迹跟踪开关
     */
    fun toggleTrace() {
        if (traceUseCase.isTracing()) {
            traceUseCase.stopTrace()
        } else {
            traceUseCase.setOnTraceSuccess { drawTraceList(it) }
            traceUseCase.startTrace()
            // TODO: 路径缓存在本地，防止未保存等情况就直接关闭app丢失数据情况
        }
        _isTracing.value = traceUseCase.isTracing()
    }

    /**
     * 切换跟踪模式
     */
    fun toggleFollowingMode() {
        val oldFollowing = _isFollowing.value ?: true
        _isFollowing.value = !oldFollowing
    }

    fun saveTrace() {
        commitData { traceUseCase.saveTraceRecord() }
    }

    fun onPause() {
        // 页面暂停时，定位会继续。但绘制会停止，所以缓存这段记录
        isPause = true
        pauseCacheDrawTraceList.clear()
    }

    fun onResume() {
        isPause = false
        if (pauseCacheDrawTraceList.size >= 2) {
            _mapEvent.value = DrawTraceLine(pauseCacheDrawTraceList)
        }
        pauseCacheDrawTraceList.clear()
    }

    private fun onLocationSuccess(location: TraceLocation) {
        _mapEvent.value = SuccessLocation(location)
        _uiState.value = TraceMapUiState(traceUseCase.getMyLocation(), _isFollowing.value!!)
    }

    private fun drawTraceList(list: ArrayList<TraceLocation>) {
        if (CollectionUtils.isEmpty(list) || list.size < 2) {
            return
        }
        if (isPause) {
            // 如果是暂停状态，则每次只记录最新一个轨迹点
            if (pauseCacheDrawTraceList.size == 0) {
                // 首次记录，还要加上暂停前最后一个轨迹点，用于连线绘制
                pauseCacheDrawTraceList.add(list[list.lastIndex - 1])
            }
            pauseCacheDrawTraceList.add(list[list.lastIndex])
        } else {
            // 如果需要立刻绘制，则绘制最新两个点之间连线
            val stepList = arrayListOf(list[list.lastIndex - 1], list[list.lastIndex])
            _mapEvent.value = DrawTraceLine(stepList)
        }
    }

}