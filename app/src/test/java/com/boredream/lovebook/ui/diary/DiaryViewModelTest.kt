package com.boredream.lovebook.ui.diary

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.boredream.lovebook.MainDispatcherRule
import com.boredream.lovebook.common.SimpleRequestSuccess
import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.repo.DiaryRepository
import com.boredream.lovebook.utils.MockUtils
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkObject
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
        vm = DiaryViewModel(repo)
    }

    @Test
    fun loadList() = runTest {
        val mockList = mockk<List<Diary>>()
        every { mockList.size } returns 10

        every {
            runBlocking {
                repo.getPageList(any())
            }
        } returns ResponseEntity.success(mockList)

        vm.start()
        Assert.assertEquals(SimpleRequestSuccess::class.java, vm.loadListUiState.value?.javaClass)

        val success : SimpleRequestSuccess<List<Diary>> = vm.loadListUiState.value as SimpleRequestSuccess<List<Diary>>
        Assert.assertEquals(10, success.data.size)
    }

}