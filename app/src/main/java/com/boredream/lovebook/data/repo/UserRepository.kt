package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseRepository
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.User
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

    suspend fun login(username: String, password: String): ResponseEntity<String> {
        val response = service.login(LoginDto(username, password))
        if (response.isSuccess()) {
            localDataSource.saveToken(response.getSuccessData())
        }
        return response
    }

    suspend fun logout() {
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

    fun getLocalToken(): String? {
        return localDataSource.getToken()
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