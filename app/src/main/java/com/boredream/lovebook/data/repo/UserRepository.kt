package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseRepository
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.User
import com.boredream.lovebook.data.constant.GlobalConstant.getLocalUser
import com.boredream.lovebook.data.dto.LoginDto
import com.boredream.lovebook.data.repo.source.UserLocalDataSource
import com.boredream.lovebook.net.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val service: ApiService,
    private val localDataSource: UserLocalDataSource
) : BaseRepository() {

    suspend fun autoLogin() : ResponseEntity<User> {
        return if(localDataSource.getToken() != null) {
            // 有本地token，然后获取最新 user info
            service.getUserInfo()
        } else {
            ResponseEntity.notExistError()
        }
    }

    suspend fun login(username: String, password: String): ResponseEntity<User> {
        val response = service.login(LoginDto(username, password))
        return if (response.isSuccess()) {
            localDataSource.saveToken(response.getSuccessData())
            getUserInfo()
        } else {
            ResponseEntity(null, response.code, response.msg)
        }
    }

    fun logout() {
        localDataSource.saveToken(null)
        localDataSource.saveUser(null)
    }

    suspend fun getUserInfo(): ResponseEntity<User> {
        val response = service.getUserInfo()
        if (response.isSuccess()) {
            localDataSource.saveUser(response.getSuccessData())
        }
        return response
    }

    fun getLocalUser(): User? {
        return localDataSource.getUser()
    }

    suspend fun updateTogetherDay(togetherDay: String): ResponseEntity<Boolean> {
        val curUser = getLocalUser() ?: return ResponseEntity.notExistError()
        curUser.cpTogetherDate = togetherDay
        val response = service.updateUserInfo(curUser.id!!, curUser)
        if (response.isSuccess()) {
            localDataSource.saveUser(curUser)
        }
        return response
    }

}