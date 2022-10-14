package com.boredream.lovebook.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * ViewModel 负责连接视图和数据，主要的逻辑处理都在此进行。
 * 不能持有任何 Context / View 相关的应用。
 */
open class BaseViewModel : ViewModel() {

    // 通用的UI状态
    protected val _baseUiState = MutableLiveData<BaseUiState>()
    val baseUiState: LiveData<BaseUiState> = _baseUiState

}