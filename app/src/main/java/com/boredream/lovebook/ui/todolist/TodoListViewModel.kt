package com.boredream.lovebook.ui.todolist

import androidx.lifecycle.viewModelScope
import com.boredream.lovebook.common.SimpleListViewModel
import com.boredream.lovebook.common.vmcompose.RequestVMCompose
import com.boredream.lovebook.data.Todo
import com.boredream.lovebook.data.TodoGroup
import com.boredream.lovebook.data.repo.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository
) : SimpleListViewModel<Todo>() {

    lateinit var todoGroup: TodoGroup
    override fun isPageList() = false
    override suspend fun repoDeleteRequest(data: Todo) = repository.delete(data.id!!)
    override suspend fun repoListRequest(forceRemote: Boolean) =
        repository.getList(forceRemote, todoGroup.id!!)

    val updateVMCompose = RequestVMCompose<Boolean>(viewModelScope)
    fun doneTodo(data: Todo) {
        updateVMCompose.request { repository.update(data) }
    }

}