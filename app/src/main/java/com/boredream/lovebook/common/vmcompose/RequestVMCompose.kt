package com.boredream.lovebook.common.vmcompose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.boredream.lovebook.base.BaseLiveEvent
import com.boredream.lovebook.base.BaseUiState
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.vm.SingleLiveEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * 请求类 view model 组件
 * 不影响架构，用于节省模版代码
 * VM + View ... 应该是配套的一组
 */
class RequestVMCompose<T>(
    private val viewModelScope: CoroutineScope,
    private val _baseUiState: MutableLiveData<BaseUiState>
) {
    private var fetchJob: Job? = null

    private val _failUiState = MutableLiveData<ResponseEntity<T>>()
    val failUiState: LiveData<ResponseEntity<T>> = _failUiState

    private val _successUiState = MutableLiveData<ResponseEntity<T>>()
    val successUiState: LiveData<ResponseEntity<T>> = _successUiState

    fun request(onSuccess: () -> Unit = {}, repoRequest: suspend () -> ResponseEntity<T>) {
        // TODO: 不传进来，有更好的方法吗？
        _baseUiState.value = BaseUiState(showLoading = true)

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val response = repoRequest.invoke()
                _baseUiState.value = BaseUiState(showLoading = false)

                if (response.isSuccess()) {
                    _successUiState.value = response
                    onSuccess.invoke()
                } else {
                    _failUiState.value = response
                }
            } catch (e: Exception) {
                _failUiState.value = ResponseEntity.httpError(e)
            }
        }
    }

}