package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseLoadRepository
import com.boredream.lovebook.base.BaseRepository
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.Todo
import com.boredream.lovebook.net.ServiceFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(serviceFactory: ServiceFactory) : BaseLoadRepository<Todo>(serviceFactory) {

    suspend fun getList(groupId: String) = getList { service.getTodoList(groupId) }
    suspend fun add(data: Todo) = commit { service.addTodo(data) }
    suspend fun update(data: Todo) = commit { service.updateTodo(data.id!!, data) }
    suspend fun delete(id: String) = commit { service.deleteTodo(id) }

}