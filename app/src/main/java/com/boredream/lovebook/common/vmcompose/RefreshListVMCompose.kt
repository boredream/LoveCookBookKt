package com.boredream.lovebook.common.vmcompose

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
)

/**
 * 列表类 view model 组件
 * 不影响架构，用于节省模版代码
 * VM + View ... 应该是配套的一组
 */
class RefreshListVMCompose(private val viewModelScope: CoroutineScope) {

    private val _refreshUiState = MutableLiveData<RefreshUiState>()
    val refreshUiState: LiveData<RefreshUiState> = _refreshUiState

    private val _dataListUiState = MutableLiveData<ArrayList<*>>()
    val dataListUiState: LiveData<ArrayList<*>> = _dataListUiState

    fun <T> loadList(
        handlePullDownDown: Boolean = true,
        loadMore: Boolean = false,
        repoRequest: suspend (loadMore: Boolean) -> ResponseEntity<ListResult<T>>,
    ) {
        // 只有非手动下拉刷新，才需要主动显示下拉样式
        if(!handlePullDownDown && !loadMore) {
            _refreshUiState.value = RefreshUiState(showRefresh = true)
        }

        viewModelScope.launch {
            val response = repoRequest.invoke(loadMore)
            var hasMore = loadMore
            if (response.isSuccess()) {
                // 是否还有更多，根据返回数据判断；如果无返回数据，保留原有意图
                response.data?.hasMore?.let { hasMore = it }
                val dataList = response.data?.dataList?: ArrayList()
                _dataListUiState.value = dataList
            } else {
                // 请求失败的时候应该继续保持当前列表数据
            }

            _refreshUiState.value = RefreshUiState(enableLoadMore = hasMore)
        }
    }

}