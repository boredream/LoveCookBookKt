package com.boredream.lovebook.data.repo

import com.boredream.lovebook.TestDataConstants
import com.boredream.lovebook.data.repo.source.UserLocalDataSource
import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.net.ServiceCreator
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {

    private lateinit var localDataSource: UserLocalDataSource
    private lateinit var repo: UserRepository

    @Before
    fun setUp() {
        localDataSource = mockk()
        every {
            localDataSource.saveUser(any())
        } returns Unit
        every {
            localDataSource.saveToken(any())
        } returns Unit
        every { localDataSource.getToken() } returns TestDataConstants.token
        every { localDataSource.getUser() } returns TestDataConstants.user

        repo = UserRepository(TestDataConstants.getApiService(), localDataSource)
    }

    @Test
    fun login() = runTest {
        val response = repo.login("18501683422", "123456")
        println(response)
        assertNotNull(response.data)
    }

    @Test
    fun getUserInfo() = runTest {
        val response = repo.getUserInfo()
        println(response)
        assertNotNull(response.data)
    }

    @Test
    fun setTogetherDay() = runTest {
        val response = repo.updateTogetherDay("2020-02-14")
        println(response)
        assertNotNull(response.data)
    }


}