package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseRequestRepository
import com.boredream.lovebook.data.TodoGroup
import com.boredream.lovebook.net.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoGroupRepository @Inject constructor(private val service: ApiService) :
    BaseRequestRepository<TodoGroup>() {

    suspend fun getList(forceRemote: Boolean) =
        getList(forceRemote) { service.getTodoGroupList() }

    suspend fun add(data: TodoGroup) = commit { service.addTodoGroup(data) }
    suspend fun update(data: TodoGroup) = commit { service.updateTodoGroup(data.id!!, data) }
    suspend fun delete(id: String) = commitDelete(id) { service.deleteTodoGroup(id) }

}