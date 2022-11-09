package com.boredream.lovebook.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.dto.PageResultDto
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * 带请求的 view model
 * 不影响架构，用于节省模版代码
 */
open class BaseRequestViewModel<T> : BaseViewModel() {

    // 主job，保证同一时间只能一个在工作
    private var fetchJob: Job? = null

    private val _loadPageUiState = MutableLiveData<SimpleRequestUiState<PageResultDto<T>>>()
    val loadPageUiState: LiveData<SimpleRequestUiState<PageResultDto<T>>> = _loadPageUiState

    private val _loadListUiState = MutableLiveData<SimpleRequestUiState<List<T>>>()
    val loadListUiState: LiveData<SimpleRequestUiState<List<T>>> = _loadListUiState

    private val _loadDataUiState = MutableLiveData<SimpleRequestUiState<T>>()
    val loadDataUiState: LiveData<SimpleRequestUiState<T>> = _loadDataUiState

    private val _commitDataUiState = MutableLiveData<SimpleRequestUiState<Boolean>>()
    val commitDataUiState: LiveData<SimpleRequestUiState<Boolean>> = _commitDataUiState

    /**
     * 处理单个数据（CUD）
     */
    protected fun commitData(repoRequest: suspend () -> ResponseEntity<Boolean>) {
        request(_commitDataUiState, repoRequest)
    }

    /**
     * 加载单个数据
     */
    protected fun loadSingle(repoRequest: suspend () -> ResponseEntity<T>) {
        request(_loadDataUiState, repoRequest)
    }

    /**
     * 加载列表数据
     */
    protected fun loadList(repoRequest: suspend () -> ResponseEntity<List<T>>) {
        request(_loadListUiState, repoRequest)
    }

    /**
     * 加载分页数据
     */
    protected fun loadPage(repoRequest: suspend () -> ResponseEntity<PageResultDto<T>>) {
        request(_loadPageUiState, repoRequest)
    }

    private fun <UiState> request(
        requestUiState: MutableLiveData<SimpleRequestUiState<UiState>>,
        repoRequest: suspend () -> ResponseEntity<UiState>
    ) {
        _baseUiState.value = BaseUiState(showLoading = true)

        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                val response = repoRequest.invoke()
                _baseUiState.value = BaseUiState(showLoading = false)

                if (response.isSuccess()) {
                    requestUiState.value = SimpleRequestSuccess(response.getSuccessData())
                } else {
                    requestUiState.value = SimpleRequestFail(response.msg)
                }
            } catch (e: Exception) {
                requestUiState.value = SimpleRequestFail(e.message ?: "请求错误 $e")
            }
        }
    }

}