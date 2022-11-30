package com.boredream.lovebook.base.refreshlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.dto.ListResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class RefreshUiState(
    val showRefresh: Boolean = false,
    val enableLoadMore: Boolean = false,
    val showLoadMore: Boolean = false,
    val list: ArrayList<*>? = null,
)

/**
 * 列表类 view model 组件
 * 不影响架构，用于节省模版代码
 * VM + View ... 应该是配套的一组
 */
class RefreshListVMCompose(private val viewModelScope: CoroutineScope) {

    private val _uiState = MutableLiveData<RefreshUiState>()
    val uiState: LiveData<RefreshUiState> = _uiState

    fun <T> loadList(
        handlePullDownDown: Boolean = true,
        loadMore: Boolean = false,
        repoRequest: suspend (loadMore: Boolean) -> ResponseEntity<ListResult<T>>,
    ) {
        // 只有非手动下拉刷新，才需要主动显示下拉样式
        _uiState.value = RefreshUiState(showRefresh = !handlePullDownDown && !loadMore)

        viewModelScope.launch {
            val response = repoRequest.invoke(loadMore)
            if (response.isSuccess()) {
                // 请求数据成功返回
                val hasMore = response.data?.hasMore ?: false
                val dataList = response.data?.dataList
                _uiState.value = RefreshUiState(
                    enableLoadMore = hasMore,
                    list = dataList,
                )
            } else {
                // TODO loadMore 请求失败的时候应该继续保持当前列表数据
                _uiState.value = RefreshUiState()
            }
        }
    }

}