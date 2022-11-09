package com.boredream.lovebook.ui.todolist

import androidx.lifecycle.LiveData
import com.boredream.lovebook.base.BaseRequestViewModel
import com.boredream.lovebook.data.Todo
import com.boredream.lovebook.data.repo.TodoRepository
import com.boredream.lovebook.vm.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository
) : BaseRequestViewModel<Todo>() {

    private val _toDetailEvent = SingleLiveEvent<Boolean>()
    val toDetailEvent: LiveData<Boolean> = _toDetailEvent

    init {
        // vm(页面) 创建时，清空缓存标志位，重新拉取接口
        repository.cacheIsDirty = false
    }

    fun start(groupId: String) {
        // TODO: 接口不会再次请求，但列表view会notify？
        loadList { repository.getList(groupId) }
    }

    fun startAdd() {
        // TODO: 应该这种方式封装吗？
        // _baseEvent.value = StartActivityLiveEvent(TodoDetailActivity::class.java)
        _toDetailEvent.value = true
    }

    fun delete(data: Todo) {
        commitData { repository.delete(data.id!!) }
    }

}