package com.boredream.lovebook.ui.trace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.blankj.utilcode.util.CollectionUtils
import com.boredream.lovebook.base.BaseViewModel
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.usecase.TraceUseCase
import com.boredream.lovebook.vm.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TraceMapViewModel @Inject constructor(
    private val traceUseCase: TraceUseCase
) : BaseViewModel() {

    // TODO: 如何更好的设计地图这种 命令式传统view 和 vm 的关系？

    private var firstLocation = true
    private var isPause = false
    private var pauseCacheDrawTraceList = ArrayList<ArrayList<TraceLocation>>()

    private val _mapEvent = SingleLiveEvent<MapUiEvent>()
    val mapEvent: LiveData<MapUiEvent> = _mapEvent

    private val _uiState = MutableLiveData<TraceMapUiState>()
    val uiState : LiveData<TraceMapUiState> = _uiState

    fun startLocation() {
        traceUseCase.setOnLocationSuccess { onLocationSuccess(it) }
        traceUseCase.startLocation()
    }

    fun stopLocation() {
        traceUseCase.stopLocation()
    }

    fun toggleTrace() {
        TODO("Not yet implemented")
    }

    fun startTrace() {
        traceUseCase.setOnTraceSuccess { drawTraceList(it) }
        traceUseCase.startTrace()
    }

    fun stopTrace() {
        TODO("Not yet implemented")
    }

    fun locationMe() {
        traceUseCase.getMyLocation()?.let { _mapEvent.value = MoveToLocation(it) }
    }

    fun onPause() {
        // 页面暂停时，定位会继续。但绘制会停止，所以缓存这段记录
        isPause = true
        pauseCacheDrawTraceList.clear()
    }

    fun onResume() {
        isPause = false
        pauseCacheDrawTraceList.forEach { drawTraceList(it) }
        pauseCacheDrawTraceList.clear()
    }

    private fun onLocationSuccess(location: TraceLocation) {
        if (firstLocation) {
            locationMe()
        }
        firstLocation = false

        _mapEvent.value = SuccessLocation(location)
        _uiState.value = TraceMapUiState(myLocation = traceUseCase.getMyLocation())
    }

    private fun drawTraceList(list: ArrayList<TraceLocation>) {
        if (CollectionUtils.isEmpty(list) || list.size < 2) {
            return
        }
        val stepList = arrayListOf(list[list.lastIndex - 1], list[list.lastIndex])
        if(isPause) {
            pauseCacheDrawTraceList.add(stepList)
        } else {
            _mapEvent.value = DrawTraceLine(stepList)
        }
    }

}