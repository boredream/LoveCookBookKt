package com.boredream.lovebook.ui.trace.recorddetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.boredream.lovebook.base.BaseViewModel
import com.boredream.lovebook.common.vmcompose.RequestVMCompose
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.TraceRecord
import com.boredream.lovebook.data.usecase.TraceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TraceRecordDetailViewModel @Inject constructor(
    private val useCase: TraceUseCase
) : BaseViewModel() {

    val requestVMCompose = RequestVMCompose<ArrayList<TraceLocation>>(viewModelScope)

    private val _traceListUiState = MutableLiveData(ArrayList<ArrayList<TraceLocation>>())
    val traceListUiState: LiveData<ArrayList<ArrayList<TraceLocation>>> = _traceListUiState

    private val _startLocationUiState = MutableLiveData<TraceLocation>()
    val startLocationUiState: LiveData<TraceLocation> = _startLocationUiState

    /**
     * 页面开始时，绘制路线，并跳转到对应位置
     */
    fun start(data: TraceRecord) {
        requestVMCompose.request(
            onSuccess = { updateTraceList(it) },
            repoRequest = { useCase.getTraceList(data.id!!) })
    }

    private fun updateTraceList(traceList: ArrayList<TraceLocation>?) {
        traceList?.let {
            _traceListUiState.value = arrayListOf(it)
            // TODO:  根据路线，选择合适的 camera zoom 和 position
            _startLocationUiState.value = it[it.lastIndex]
        }
    }

}