package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseRepository
import com.boredream.lovebook.data.Todo
import com.boredream.lovebook.net.ServiceFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(serviceFactory: ServiceFactory) : BaseRepository(serviceFactory) {

    suspend fun getList(groupId: String) = tryHttpError { service.getTodoList(groupId) }
    suspend fun add(data: Todo) = tryHttpError { service.addTodo(data) }
    suspend fun update(data: Todo) = tryHttpError { service.updateTodo(data, data.id!!) }
    suspend fun delete(id: String) = tryHttpError { service.deleteTodo(id) }

}