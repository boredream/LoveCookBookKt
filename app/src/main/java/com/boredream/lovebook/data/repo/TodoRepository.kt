package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseRequestRepository
import com.boredream.lovebook.data.Todo
import com.boredream.lovebook.net.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(private val service: ApiService) :
    BaseRequestRepository<Todo>(service) {

    suspend fun getList(forceRemote: Boolean, groupId: String) =
        getList(forceRemote) { service.getTodoList(groupId) }

    suspend fun add(data: Todo) = commit { service.addTodo(data) }
    suspend fun update(data: Todo) = commit { service.updateTodo(data.id!!, data) }
    suspend fun delete(id: String) = commit { service.deleteTodo(id) }

}