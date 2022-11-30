package com.boredream.lovebook.base

import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.dto.PageResultDto
import com.boredream.lovebook.data.dto.ListResult
import com.boredream.lovebook.net.ApiService

abstract class BaseLoadRepository<T : BaseEntity> : BaseRepository() {

    var cacheListPage = 1
    var cacheList: ArrayList<T> = ArrayList()
    var cacheListCanLoadMore = false
    var cacheIsDirty = true

    protected suspend fun getPageList(forceRemote: Boolean = false,
                                      loadMore: Boolean = false,
                                      request: suspend (page: Int) -> ResponseEntity<PageResultDto<T>>): ResponseEntity<ListResult<T>> {
        if (!forceRemote && !cacheIsDirty && !loadMore) {
            // 非强制远程，且缓存数据有效，且是非加载更多模式时，直接返回
            return ResponseEntity.success(ListResult(cacheListCanLoadMore, cacheList))
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

        // 数据转换一层，PageResult -> ListResult
        return ResponseEntity(ListResult(cacheListCanLoadMore, cacheList), response.code, response.msg)
    }

    protected suspend fun getList(forceRemote: Boolean = false, request: suspend () -> ResponseEntity<List<T>>): ResponseEntity<ListResult<T>> {
        if (!forceRemote && !cacheIsDirty) {
            // 非强制远程，且缓存数据有效时，直接返回
            return ResponseEntity.success(ListResult(false, cacheList))
        }

        val response: ResponseEntity<List<T>> = tryHttpError { request.invoke() }
        if (response.isSuccess()) {
            // TODO: db local data source
            // 缓存在本地
            cacheList.clear()
            cacheList.addAll(response.getSuccessData())
            cacheIsDirty = false
        }

        // 数据转换一层，List -> ListResult
        return ResponseEntity(ListResult(cacheListCanLoadMore, cacheList), response.code, response.msg)
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