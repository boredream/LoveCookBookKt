package com.boredream.lovebook.base

import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.net.ApiService

/**
 * 数据仓库，单例，全局复用
 */
open class BaseRepository {

    /**
     * http请求预处理
     */
    protected suspend fun <T> tryHttpError(request: suspend () -> ResponseEntity<T>) : ResponseEntity<T> {
        return try {
            request.invoke()
        } catch (e: Exception) {
            ResponseEntity.httpError(e)
        }
    }

}