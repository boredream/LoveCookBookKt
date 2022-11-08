package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseRepository
import com.boredream.lovebook.data.Todo
import com.boredream.lovebook.data.TodoGroup
import com.boredream.lovebook.net.ServiceFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(serviceFactory: ServiceFactory) : BaseRepository(serviceFactory) {

    suspend fun getGroupList() = service.getTodoGroupList()
    suspend fun addGroup(data: TodoGroup) = service.addTodoGroup(data)
    suspend fun updateGroup(data: TodoGroup) = service.updateTodoGroup(data, data.id!!)
    suspend fun deleteGroup(id: String) = service.deleteTodoGroup(id)

    // TODO: 需要分开吗？

    suspend fun getList(groupId: String) = service.getTodoList(groupId)
    suspend fun add(data: Todo) = service.addTodo(data)
    suspend fun update(data: Todo) = service.updateTodo(data, data.id!!)
    suspend fun delete(id: String) = service.deleteTodo(id)

}