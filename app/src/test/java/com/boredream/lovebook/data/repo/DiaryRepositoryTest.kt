package com.boredream.lovebook.data.repo

import com.boredream.lovebook.TestDataConstants
import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.net.ServiceFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DiaryRepositoryTest {

    private lateinit var repo: DiaryRepository

    @Before
    fun setUp() {
        val factory = ServiceFactory()
        factory.testToken = TestDataConstants.token
        repo = DiaryRepository(factory)
    }

    @Test
    fun testAll() = runTest {
        // add
        val data = Diary()
        data.id = "99"
        data.content = "test diary"
        data.diaryDate = "2050-02-14"
        val addResponse = repo.add(data)
        assertTrue(addResponse.getSuccessData())

        // update
        data.content += " update"
        val updateResponse = repo.update(data)
        assertTrue(updateResponse.getSuccessData())

        // query
        val getResponse = repo.getList(1)
        val records = getResponse.getSuccessData().records
        assertNotNull(records)
        assertNotSame(0, records.size)
        var matchData: Diary? = null
        for (record in records) {
            record.id = data.id
            matchData = record
            break
        }
        assertNotNull(matchData)
        assertEquals("test diary update", matchData!!.content)

        // delete
        val deleteResponse = repo.delete("99")
        assertTrue(deleteResponse.getSuccessData())
    }

}