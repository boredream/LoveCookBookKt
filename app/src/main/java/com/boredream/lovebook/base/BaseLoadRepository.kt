package com.boredream.lovebook.base

import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.net.ServiceFactory

abstract class BaseLoadRepository<T : BaseEntity>(serviceFactory: ServiceFactory) :
    BaseRepository(serviceFactory) {

    // TODO: 如何把类的T封装到方法级里
    private var cacheList: ArrayList<T> = ArrayList()
    private var cacheIsDirty = true

    protected suspend fun getList(forceRemote: Boolean = false, request: suspend () -> ResponseEntity<List<T>>): ResponseEntity<List<T>> {
        if (!forceRemote && !cacheIsDirty) {
            // 非强制远程，且缓存数据有效时，直接返回
            return ResponseEntity.success(cacheList)
        }

        val response: ResponseEntity<List<T>> = tryHttpError { request.invoke() }
        if (response.isSuccess()) {
            // TODO: db local data source
            // 缓存在本地
            cacheList.clear()
            cacheList.addAll(response.data!!)
            cacheIsDirty = false
        }
        return response
    }

    protected suspend fun commit(request: suspend () -> ResponseEntity<Boolean>): ResponseEntity<Boolean> {
        val response: ResponseEntity<Boolean> = tryHttpError { request.invoke() }
        if (response.isSuccess()) {
            // 修改成功后，缓存需要重新刷新
            cacheIsDirty = true
        }
        return response
    }

}