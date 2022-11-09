package com.boredream.lovebook.base

import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.net.ServiceFactory

open class BaseRepository(serviceFactory: ServiceFactory) {

    protected val service: ApiService = serviceFactory.getApiService()

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