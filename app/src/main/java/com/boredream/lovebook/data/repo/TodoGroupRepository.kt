package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseRepository
import com.boredream.lovebook.data.TodoGroup
import com.boredream.lovebook.net.ServiceFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoGroupRepository @Inject constructor(serviceFactory: ServiceFactory) : BaseRepository(serviceFactory) {

    suspend fun getList() = tryHttpError { service.getTodoGroupList() }
    suspend fun add(data: TodoGroup) = tryHttpError { service.addTodoGroup(data) }
    suspend fun update(data: TodoGroup) = tryHttpError { service.updateTodoGroup(data, data.id!!) }
    suspend fun delete(id: String) = tryHttpError { service.deleteTodoGroup(id) }

}