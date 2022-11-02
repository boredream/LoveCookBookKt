package com.boredream.lovebook.ui.diary

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.boredream.lovebook.MainDispatcherRule
import com.boredream.lovebook.TestDataConstants
import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.repo.DiaryRepository
import com.boredream.lovebook.net.ServiceFactory
import com.boredream.lovebook.utils.MockUtils
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DiaryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var repo: DiaryRepository

    private lateinit var vm: DiaryViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        val factory = ServiceFactory()
        factory.testToken = TestDataConstants.token
        vm = DiaryViewModel(repo)
    }

    @Test
    fun loadList() = runTest {
        every {
            runBlocking {
                repo.getList(any())
            }
        } returns ResponseEntity.success(MockUtils.mockPageResult(Diary::class.java))

        vm.loadList()
        Assert.assertEquals(10, vm.dataList.value?.size)

        vm.loadList(loadMore = true)
        Assert.assertEquals(20, vm.dataList.value?.size)

        vm.loadList()
        Assert.assertEquals(10, vm.dataList.value?.size)
    }

}