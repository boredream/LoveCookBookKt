package com.boredream.lovebook.data.source

import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.User
import com.boredream.lovebook.data.dto.LoginDto
import retrofit2.Call
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor() : BaseRepository() {

    var token: String? = null
    var curUser: User? = null

    /**
     * 登录
     */
    suspend fun login(username: String, password: String): ResponseEntity<String> {
        val response = service.login(LoginDto(username, password))
        if (response.isSuccess()) {
            token = response.data
        }
        return response
    }

    /**
     * 登录
     */
    suspend fun getUserInfo(): ResponseEntity<User> {
        val response = service.getUserInfo()
        if (response.isSuccess()) {
            curUser = response.data
        }
        return response
    }
}