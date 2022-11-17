package com.boredream.lovebook.base

import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.dto.PageResultDto
import com.boredream.lovebook.data.repo.DiaryRepository
import com.boredream.lovebook.net.ApiService
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseLoadRepositoryTest {

    @MockK
    lateinit var apiService: ApiService

    lateinit var repo: DiaryRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repo = DiaryRepository(apiService)
    }

    @Test
    fun testLoadPage() = runTest {
        val mockList = ArrayList<Diary>()
        for(i in 1..10) {
            mockList.add(mockk())
        }

        // 加载第一页
        val page1Result = PageResultDto(1, 10, 20, 2, mockList)
        every {
            runBlocking { apiService.getDiaryList(1, any()) }
        } returns ResponseEntity.success(page1Result)
        repo.getPageList(false)
        Assert.assertEquals(1, repo.cacheListPage)
        Assert.assertEquals(10, repo.cacheList.size)
        Assert.assertEquals(true, repo.cacheListCanLoadMore)

        // 加载第二页
        val page2Result = PageResultDto(2, 10, 20, 2, mockList)
        every {
            runBlocking { apiService.getDiaryList(2, any()) }
        } returns ResponseEntity.success(page2Result)
        repo.getPageList(true)
        Assert.assertEquals(2, repo.cacheListPage)
        Assert.assertEquals(20, repo.cacheList.size)
        Assert.assertEquals(false, repo.cacheListCanLoadMore)
    }

}
