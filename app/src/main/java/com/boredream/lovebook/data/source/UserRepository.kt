package com.boredream.lovebook.data.source

import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.User

object UserRepository: BaseRepository() {

    var token: String? = null
    var curUser: User? = null

    /**
     * 登录
     */
    suspend fun login(username: String, password: String): ResponseEntity<String> {
        val response = service.login(username, password)
        if(response.isSuccess()) {
            token = response.data
        }
        return response
    }

    /**
     * 登录
     */
    suspend fun getUserInfo(): ResponseEntity<User> {
        val response = service.getUserInfo()
        if(response.isSuccess()) {
            curUser = response.data
        }
        return response
    }

}