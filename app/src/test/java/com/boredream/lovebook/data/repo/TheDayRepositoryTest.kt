package com.boredream.lovebook.data.repo

import com.boredream.lovebook.TestDataConstants
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.net.ServiceFactory
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class TheDayRepositoryTest {

    private lateinit var repo: TheDayRepository

    @Before
    fun setUp() {
        val factory = ServiceFactory()
        factory.testToken = TestDataConstants.token
        repo = TheDayRepository(factory)
    }

    @Test
    fun getList() {
        runBlocking {
            val response = repo.getList()
            print(response)
            assertNotNull(response.data)
        }
    }

    @Test
    fun add() {
        runBlocking {
            val theDay = TheDay()
            theDay.name = "test"
            theDay.theDayDate = "1900-02-14"
            val response = repo.add(theDay)
            assertTrue(response.getSuccessData())
        }
    }

    @Test
    fun update() {
        runBlocking {
            val theDay = TheDay()
            theDay.id = "22"
            theDay.name = "test"
            theDay.theDayDate = "1990-02-14"
            val response = repo.update(theDay)
            assertTrue(response.getSuccessData())
        }
    }

    @Test
    fun delete() {
        runBlocking {
            val response = repo.delete("22")
            assertTrue(response.getSuccessData())
        }
    }

}