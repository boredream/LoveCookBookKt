package com.boredream.lovebook.data.source

import com.boredream.lovebook.data.ResponseEntity

class UserRepository: BaseRepository() {

    /**
     * 登录
     */
    suspend fun login(username: String, password: String): ResponseEntity<String> {
        return service.login(username, password)
    }

}