package com.boredream.lovebook.data.repo

import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.User
import com.boredream.lovebook.data.constant.GlobalConstant
import com.boredream.lovebook.data.dto.LoginDto
import com.boredream.lovebook.data.repo.source.UserLocalDataSource
import com.boredream.lovebook.net.ServiceFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    serviceFactory: ServiceFactory,
    private val localDataSource: UserLocalDataSource
) : BaseRepository(serviceFactory) {

    var curUser: User? = null

    /**
     * 登录
     */
    suspend fun login(username: String, password: String): ResponseEntity<String> {
        val response = service.login(LoginDto(username, password))
        if (response.isSuccess()) {
            localDataSource.saveToken(response.data)
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
            localDataSource.saveUser(response.data)
        }
        return response
    }

    fun getLocalUser(): User? {
        return localDataSource.getUser()
    }

}