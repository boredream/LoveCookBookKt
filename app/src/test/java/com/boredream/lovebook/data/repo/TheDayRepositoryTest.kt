package com.boredream.lovebook.data.repo

import com.boredream.lovebook.TestDataConstants
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.net.ServiceFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TheDayRepositoryTest {

    private lateinit var repo: TheDayRepository

    @Before
    fun setUp() {
        val factory = ServiceFactory()
        factory.testToken = TestDataConstants.token
        repo = TheDayRepository(factory)
    }

    @Test
    fun getList() = runTest {
        val response = repo.getList()
        print(response)
        assertNotNull(response.data)
    }

    @Test
    fun add() = runTest {
        val theDay = TheDay()
        theDay.name = "test"
        theDay.theDayDate = "1900-02-14"
        val response = repo.add(theDay)
        assertTrue(response.getSuccessData())
    }

    @Test
    fun update() = runTest {
        val theDay = TheDay()
        theDay.id = "22"
        theDay.name = "test"
        theDay.theDayDate = "1990-02-14"
        val response = repo.update(theDay)
        assertTrue(response.getSuccessData())
    }

    @Test
    fun delete() = runTest {
        val response = repo.delete("22")
        assertTrue(response.getSuccessData())
    }

}