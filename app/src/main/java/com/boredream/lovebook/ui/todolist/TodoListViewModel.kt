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

    fun startAdd() {
        // TODO: 应该这种方式封装吗？
        // _baseEvent.value = StartActivityLiveEvent(TodoDetailActivity::class.java)
        _toDetailEvent.value = true
    }

    fun delete(data: Todo) {
        commitData { repository.delete(data.id!!) }
    }

    fun loadList(groupId: String) {
        loadList { repository.getList(groupId) }
    }

}