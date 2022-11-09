package com.boredream.lovebook.base

import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.net.ServiceFactory

abstract class BaseLoadRepository<T : BaseEntity>(serviceFactory: ServiceFactory) :
    BaseRepository(serviceFactory) {

    private var cacheList: ArrayList<T> = ArrayList()
    private var cacheIsDirty = true

    suspend fun getList(forceRemote: Boolean = false): ResponseEntity<List<T>> {
        if (!forceRemote && !cacheIsDirty) {
            // 非强制远程，且缓存数据有效时，直接返回
            return ResponseEntity.success(cacheList)
        }

        val response: ResponseEntity<List<T>> = tryHttpError { getListRemote() }
        if (response.isSuccess()) {
            // TODO: db local data source
            // 缓存在本地
            cacheList.clear()
            cacheList.addAll(response.data!!)
        }
        return response
    }

    suspend fun add(data: T): ResponseEntity<Boolean> {
        val response: ResponseEntity<Boolean> = tryHttpError { addRemote(data) }
        if (response.isSuccess()) {
            // 修改成功后，缓存需要重新刷新
            cacheIsDirty = true
        }
        return response
    }

    suspend fun update(data: T): ResponseEntity<Boolean> {
        val response: ResponseEntity<Boolean> = tryHttpError { updateRemote(data.id!!, data) }
        if (response.isSuccess()) {
            // 修改成功后，缓存需要重新刷新
            cacheIsDirty = true
        }
        return response
    }

    suspend fun delete(id: String): ResponseEntity<Boolean> {
        val response: ResponseEntity<Boolean> = tryHttpError { deleteRemote(id) }
        if (response.isSuccess()) {
            // 修改成功后，缓存需要重新刷新
            cacheIsDirty = true
        }
        return response
    }

    protected abstract suspend fun getListRemote(): ResponseEntity<List<T>>
    protected abstract suspend fun addRemote(data: T): ResponseEntity<Boolean>
    protected abstract suspend fun updateRemote(id: String, data: T): ResponseEntity<Boolean>
    protected abstract suspend fun deleteRemote(id: String): ResponseEntity<Boolean>

}