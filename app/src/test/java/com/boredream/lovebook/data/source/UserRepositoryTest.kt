package com.boredream.lovebook.data.source

import com.boredream.lovebook.TestConstants
import com.boredream.lovebook.net.ServiceFactory
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class UserRepositoryTest {

    private lateinit var repo: UserRepository

    @Before
    fun setUp() {
        val factory = ServiceFactory()
        factory.isTest = true
        factory.testToken = TestConstants.token
        repo = UserRepository(factory)
    }

    @Test
    fun login() {
        runBlocking {
            val response = repo.login("18501683421", "123456")
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
}