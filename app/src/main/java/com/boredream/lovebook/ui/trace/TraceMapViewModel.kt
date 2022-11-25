package com.boredream.lovebook.ui.trace

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.boredream.lovebook.base.BaseRequestViewModel
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.usecase.TraceUseCase
import com.boredream.lovebook.vm.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class UIEvent
object ShowSaveConfirmDialog : UIEvent()

sealed class MapUIEvent
data class SuccessLocation(val location: TraceLocation) : MapUIEvent()

data class UiState(
    val myLocation: TraceLocation? = null,
)

@HiltViewModel
class TraceMapViewModel @Inject constructor(
    private val traceUseCase: TraceUseCase
) : BaseRequestViewModel<TraceRecord>() {

    // 自定义 DataBinding 连接地图这种 命令式传统view 和 vm 的关系

    // 地图事件
    private val _mapEvent = SingleLiveEvent<MapUIEvent>()
    val mapEvent: LiveData<MapUIEvent> = _mapEvent

    // 主UI事件
    private val _uiEvent = SingleLiveEvent<UIEvent>()
    val uiEvent: LiveData<UIEvent> = _uiEvent

    // 单独更新的需要各自LiveData

    // 主UI元素
    private val _uiState = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState

    private val _tracePointListUiState = MutableLiveData(ArrayList<TraceLocation>())
    val tracePointListUiState: LiveData<ArrayList<TraceLocation>> = _tracePointListUiState

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
            // 停止轨迹跟踪后，提示保存路径
            _uiEvent.value = ShowSaveConfirmDialog

            // TODO: 路径缓存在本地，防止未保存等情况就直接关闭app丢失数据情况
        } else {
            traceUseCase.setOnTraceSuccess { onTraceSuccess(it) }
            traceUseCase.startTrace()
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

    /**
     * 保存当前轨迹
     */
    fun saveTrace() {
        commitData { traceUseCase.saveTraceRecord() }
    }

    /**
     * 放弃当前轨迹
     */
    fun abandonTrace() {
        traceUseCase.clearTrace()
        // TODO: 刷新地图上轨迹线
    }

    fun onPause() {

    }

    fun onResume() {

    }

    private fun onLocationSuccess(location: TraceLocation) {
        _mapEvent.value = SuccessLocation(location)
        _uiState.value = UiState(traceUseCase.getMyLocation())
    }

    private fun onTraceSuccess(allTracePointList: ArrayList<TraceLocation>) {
        // 轨迹成功回调，回调的是整个轨迹列表
        _tracePointListUiState.value = allTracePointList
    }

}