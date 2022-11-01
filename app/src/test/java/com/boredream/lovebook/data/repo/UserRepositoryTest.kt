package com.boredream.lovebook.data.repo

import com.boredream.lovebook.TestDataConstants
import com.boredream.lovebook.data.repo.source.UserLocalDataSource
import com.boredream.lovebook.net.ServiceFactory
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class UserRepositoryTest {

    private lateinit var localDataSource: UserLocalDataSource
    private lateinit var repo: UserRepository

    @Before
    fun setUp() {
        val factory = ServiceFactory()
        factory.testToken = TestDataConstants.token

        localDataSource = mockk()
        every {
            runBlocking {
                localDataSource.saveUser(any())
            }
        } returns Unit
        every {
            runBlocking {
                localDataSource.saveToken(any())
            }
        } returns Unit
        every { localDataSource.getToken() } returns TestDataConstants.token
        every { localDataSource.getUser() } returns TestDataConstants.user

        repo = UserRepository(factory, localDataSource)
    }

    @Test
    fun login() {
        runBlocking {
            val response = repo.login("18501683422", "123456")
            print(response)
            assertNotNull(response.data)
        }
    }

    @Test
    fun getUserInfo() {
        runBlocking {
            val response = repo.getUserInfo()
            print(response)
            assertNotNull(response.data)
        }
    }

    @Test
    fun setTogetherDay() {
        runBlocking {
            val response = repo.updateTogetherDay("2020-02-14")
            print(response)
            assertNotNull(response.data)
        }
    }


}