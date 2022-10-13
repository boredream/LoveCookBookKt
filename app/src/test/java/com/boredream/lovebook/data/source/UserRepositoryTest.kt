package com.boredream.lovebook.data.source

import org.junit.Test

class UserRepositoryTest {

    private val repo = UserRepository()

    @Test
    suspend fun testLogin() {
        val response = repo.login("18501683421", "123456")
        println(response.data)
    }

}