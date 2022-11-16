package com.boredream.lovebook.base

import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.dto.PageResultDto
import com.boredream.lovebook.net.ServiceFactory

abstract class BaseLoadRepository<T : BaseEntity>(serviceFactory: ServiceFactory) :
    BaseRepository(serviceFactory) {

    var cacheListPage = 1
    var cacheList: ArrayList<T> = ArrayList()
    var cacheListCanLoadMore = false
    var cacheIsDirty = true

    protected suspend fun getPageList(forceRemote: Boolean = false,
                                      loadMore: Boolean = false,
                                      request: suspend (page: Int) -> ResponseEntity<PageResultDto<T>>): ResponseEntity<List<T>> {
        if (!forceRemote && !cacheIsDirty && !loadMore) {
            // 非强制远程，且缓存数据有效，且是非加载更多模式时，直接返回
            return ResponseEntity.success(cacheList)
        }

        val requestPage = if(loadMore) (cacheListPage + 1) else 1
        val response: ResponseEntity<PageResultDto<T>> = tryHttpError { request.invoke(requestPage) }
        if (response.isSuccess()) {
            // TODO: db local data source
            // 缓存在本地
            val responseData = response.getSuccessData()
            cacheListPage = requestPage
            if(!loadMore) cacheList.clear()
            cacheList.addAll(responseData.records)
            cacheListCanLoadMore = responseData.current < responseData.pages
            cacheIsDirty = false
        }
        return ResponseEntity.success(cacheList)
    }

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
            cacheList.addAll(response.getSuccessData())
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