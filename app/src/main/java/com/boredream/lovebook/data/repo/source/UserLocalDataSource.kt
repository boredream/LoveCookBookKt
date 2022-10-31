package com.boredream.lovebook.data.repo.source

import com.boredream.lovebook.data.User
import com.boredream.lovebook.data.constant.GlobalConstant
import javax.inject.Inject

class UserLocalDataSource @Inject constructor() {

    // 包一层作用在于 unit test 时可以 mock

    suspend fun saveToken(token: String) {
        GlobalConstant.saveToken(token)
    }

    fun getToken(): String? {
        return GlobalConstant.getLocalToken()
    }

    suspend fun saveUser(user: User) {
        GlobalConstant.saveUser(user)
    }

    fun getUser(): User? {
        return GlobalConstant.getLocalUser()
    }

}