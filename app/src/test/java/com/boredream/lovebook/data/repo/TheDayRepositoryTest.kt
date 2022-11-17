package com.boredream.lovebook.data.repo

import com.boredream.lovebook.TestDataConstants
import com.boredream.lovebook.data.TheDay
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TheDayRepositoryTest {

    private lateinit var repo: TheDayRepository

    @Before
    fun setUp() {
        repo = TheDayRepository(TestDataConstants.getApiService())
    }

    @Test
    fun getList() = runTest {
        val response = repo.getList()
        print(response)
        assertNotNull(response.data)
    }

    @Test
    fun add() = runTest {
        val theDay = TheDay("test", "1900-02-14")
        val response = repo.add(theDay)
        assertTrue(response.getSuccessData())
    }

    @Test
    fun update() = runTest {
        val theDay = TheDay("test", "1990-02-14")
        theDay.id = "22"
        val response = repo.update(theDay)
        assertTrue(response.getSuccessData())
    }

    @Test
    fun delete() = runTest {
        val response = repo.delete("22")
        assertTrue(response.getSuccessData())
    }

}