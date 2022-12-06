package com.boredream.lovebook.base

import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.dto.ListResult
import com.boredream.lovebook.data.dto.PageResultDto
import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.net.LocalImageFilter

object RepoCacheHelper {
    val repoCache = HashMap<String, Boolean>()
}

/**
 * 请求类 Repo
 */
abstract class BaseRequestRepository<T : BaseEntity>(private val service: ApiService) :
    BaseRepository() {

    var cacheListPage = 1
    var cacheList: ArrayList<T> = ArrayList()
    var cacheListCanLoadMore = false
    var cacheIsDirty: Boolean
        get() = RepoCacheHelper.repoCache[javaClass.simpleName] ?: true
        set(value) {
            RepoCacheHelper.repoCache[javaClass.simpleName] = value
        }

    protected suspend fun getPageList(
        forceRemote: Boolean = false,
        loadMore: Boolean = false,
        request: suspend (page: Int) -> ResponseEntity<PageResultDto<T>>
    ): ResponseEntity<ListResult<T>> {
        if (!forceRemote && !cacheIsDirty && !loadMore) {
            // 非强制远程，且缓存数据有效，且是非加载更多模式时，直接返回
            return ResponseEntity.success(ListResult(cacheListCanLoadMore, cacheList))
        }

        val requestPage = if (loadMore) (cacheListPage + 1) else 1
        val response: ResponseEntity<PageResultDto<T>> =
            tryHttpError { request.invoke(requestPage) }
        if (response.isSuccess()) {
            // TODO: db local data source
            // 缓存在本地
            val responseData = response.getSuccessData()
            cacheListPage = requestPage
            if (!loadMore) cacheList.clear()
            cacheList.addAll(responseData.records)
            cacheListCanLoadMore = responseData.current < responseData.pages
            cacheIsDirty = false
        }

        // 数据转换一层，PageResult -> ListResult
        return ResponseEntity(
            ListResult(cacheListCanLoadMore, cacheList),
            response.code,
            response.msg
        )
    }

    protected suspend fun getList(
        forceRemote: Boolean = false,
        request: suspend () -> ResponseEntity<List<T>>
    ): ResponseEntity<ListResult<T>> {
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
        return ResponseEntity(
            ListResult(cacheListCanLoadMore, cacheList),
            response.code,
            response.msg
        )
    }

    /**
     * 提交请求
     * @param request SuspendFunction0<ResponseEntity<Boolean>> 请求
     * @return ResponseEntity<Boolean> 相应
     */
    protected suspend fun commit(
        request: suspend () -> ResponseEntity<Boolean>
    ): ResponseEntity<Boolean> {
        val response: ResponseEntity<Boolean> = tryHttpError { request.invoke() }
        if (response.isSuccess()) {
            // 修改成功后，缓存需要重新刷新
            cacheIsDirty = true
        }
        return response
    }

    /**
     * 提交请求
     * @param checkLocalFilePath Boolean 标志位。是否校验参数里是否有本地文件路径，有的话自动上传后路径替换成url
     * @param request SuspendFunction0<ResponseEntity<Boolean>> 请求
     * @return ResponseEntity<Boolean> 相应
     */
    protected suspend fun commitWithFile(
        checkLocalFilePath: Boolean = true,
        originData: T,
        request: suspend (data: T) -> ResponseEntity<Boolean>
    ): ResponseEntity<Boolean> {
        var data = originData
        var response: ResponseEntity<Boolean>
        try {
            if (checkLocalFilePath) data = checkUploadLocalFile(data)
            response = request.invoke(data)
        } catch (e: Exception) {
            response = ResponseEntity.httpError(e)
        }
        if (response.isSuccess()) {
            // 修改成功后，缓存需要重新刷新
            cacheIsDirty = true
        }
        return response
    }

    // 校验参数里是否有本地文件路径，有的话自动上传后路径替换成url
    private suspend fun checkUploadLocalFile(originData: T): T {
        return LocalImageFilter.checkImage4update(originData, service)
    }

    /**
     * http请求预处理
     */
    private suspend fun <T> tryHttpError(request: suspend () -> ResponseEntity<T>): ResponseEntity<T> {
        return try {
            request.invoke()
        } catch (e: Exception) {
            ResponseEntity.httpError(e)
        }
    }

}