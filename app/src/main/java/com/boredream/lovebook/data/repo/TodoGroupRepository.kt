package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseLoadRepository
import com.boredream.lovebook.data.TodoGroup
import com.boredream.lovebook.net.ServiceFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoGroupRepository @Inject constructor(serviceFactory: ServiceFactory) : BaseLoadRepository<TodoGroup>(serviceFactory) {

    override suspend fun getListRemote() = service.getTodoGroupList()
    override suspend fun addRemote(data: TodoGroup) = service.addTodoGroup(data)
    override suspend fun updateRemote(id: String, data: TodoGroup) = service.updateTodoGroup(id, data)
    override suspend fun deleteRemote(id: String) = service.deleteTodoGroup(id)

}