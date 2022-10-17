package com.boredream.lovebook

import com.boredream.lovebook.data.source.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val repo = UserRepository()
        runBlocking {
            val response = repo.login("18501683421", "123456")
            println(response)
        }
    }
}