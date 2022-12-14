package com.boredream.lovebook.ui.todogroup

import com.boredream.lovebook.common.SimpleListViewModel
import com.boredream.lovebook.data.TodoGroup
import com.boredream.lovebook.data.repo.TodoGroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TodoGroupViewModel @Inject constructor(private val repository: TodoGroupRepository) :
    SimpleListViewModel<TodoGroup>() {

    override fun isPageList() = false
    override suspend fun repoDeleteRequest(data: TodoGroup) = repository.delete(data.id!!)
    override suspend fun repoListRequest(forceRemote: Boolean) = repository.getList(forceRemote)

}