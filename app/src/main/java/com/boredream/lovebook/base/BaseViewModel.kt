package com.boredream.lovebook.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boredream.lovebook.vm.SingleLiveEvent

/**
 * ViewModel 负责连接视图和数据，主要的逻辑处理都在此进行。
 * 不能持有任何 Context / View 相关的应用。
 */
open class BaseViewModel : ViewModel() {

    // TODO: UiState 应该业务为粒度？ 还是具体UI功能为粒度？

    // 通用的UI状态
    protected val _baseUiState = MutableLiveData<BaseUiState>()
    val baseUiState: LiveData<BaseUiState> = _baseUiState

    protected val _baseEvent = SingleLiveEvent<BaseLiveEvent>()
    val baseEvent: LiveData<BaseLiveEvent> = _baseEvent

}