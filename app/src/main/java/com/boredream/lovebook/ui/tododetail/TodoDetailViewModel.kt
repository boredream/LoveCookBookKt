package com.boredream.lovebook.ui.tododetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.StringUtils
import com.boredream.lovebook.base.BaseViewModel
import com.boredream.lovebook.base.ToastLiveEvent
import com.boredream.lovebook.common.SimpleRequestFail
import com.boredream.lovebook.common.vmcompose.RequestVMCompose
import com.boredream.lovebook.data.Todo
import com.boredream.lovebook.data.repo.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject


@HiltViewModel
class TodoDetailViewModel @Inject constructor(
    private val repository: TodoRepository
) : BaseViewModel() {

    val commitVMCompose = RequestVMCompose<Boolean>(viewModelScope)

    private val _uiState = MutableLiveData<Todo>()
    val uiState: LiveData<Todo> = _uiState

    fun load(todoGroupId: String, data: Todo?) {
        // 默认今天
        _uiState.value = data ?: Todo(
            todoGroupId = todoGroupId,
            name = ""
        )
    }

    fun commit() {
        val data = _uiState.value!!

        if (StringUtils.isEmpty(data.name)) {
            _baseEvent.value = ToastLiveEvent("名字不能为空")
            return
        }

        commitVMCompose.request {
            if (data.id != null) repository.update(data)
            else repository.add(data)
        }
    }

}