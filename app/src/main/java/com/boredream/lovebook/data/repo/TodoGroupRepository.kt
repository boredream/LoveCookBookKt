package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseLoadRepository
import com.boredream.lovebook.data.TodoGroup
import com.boredream.lovebook.net.ServiceFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoGroupRepository @Inject constructor(serviceFactory: ServiceFactory) : BaseLoadRepository<TodoGroup>(serviceFactory) {

    suspend fun getList() = getList { service.getTodoGroupList() }
    suspend fun add(data: TodoGroup) = commit { service.addTodoGroup(data) }
    suspend fun update(data: TodoGroup) = commit { service.updateTodoGroup(data.id!!, data) }
    suspend fun delete(id: String) = commit { service.deleteTodoGroup(id) }

}