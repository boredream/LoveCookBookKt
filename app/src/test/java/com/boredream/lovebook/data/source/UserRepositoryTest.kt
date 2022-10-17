package com.boredream.lovebook.data.source

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class UserRepositoryTest {

    private val repo = UserRepository()

    @Test
    suspend fun testLogin() {
        val response = repo.login("18501683421", "123456")
        println(response.data)
    }

}