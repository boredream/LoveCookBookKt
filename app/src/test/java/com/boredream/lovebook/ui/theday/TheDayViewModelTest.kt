package com.boredream.lovebook.ui.theday

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.boredream.lovebook.MainDispatcherRule
import com.boredream.lovebook.TestDataConstants
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.repo.TheDayRepository
import com.boredream.lovebook.data.repo.UserRepository
import com.boredream.lovebook.net.ServiceFactory
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class TheDayViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var repo: TheDayRepository

    @MockK
    private lateinit var userRepo: UserRepository

    private lateinit var vm: TheDayViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(StandardTestDispatcher())
        val factory = ServiceFactory()
        factory.testToken = TestDataConstants.token
        vm = TheDayViewModel(repo, userRepo)
    }

    @Test
    fun loadTogetherInfo_noTogetherDay() {
        val user = TestDataConstants.createUser()
        user.cpTogetherDate = null
        every {
            userRepo.getLocalUser()
        } returns user

        vm.loadTogetherInfo()
        assertEquals("点我设置", vm.uiState.value?.togetherDayTitle)
        assertEquals("0", vm.uiState.value?.togetherDay)
    }

    @Test
    fun loadTogetherInfo_hasTogetherDay() {
        val user = TestDataConstants.createUser()
        user.cpTogetherDate = "2020-02-05"
        every {
            userRepo.getLocalUser()
        } returns user

        vm.loadTogetherInfo()
        assertEquals("我们已恋爱", vm.uiState.value?.togetherDayTitle)
        assertEquals("2020-02-05", vm.uiState.value?.togetherDay)
    }


    @Test
    fun setTogetherDay() = runTest {
        val user = TestDataConstants.createUser()
        user.cpTogetherDate = "2020-02-05"
        every {
            runBlocking {
                userRepo.updateTogetherDay(any())
            }
        } returns ResponseEntity.success(true)
        every {
            userRepo.getLocalUser()
        } returns user

        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        vm.setTogetherDay("2020-02-05")
        assertNotNull(vm.uiState.value)
        assertEquals("我们已恋爱", vm.uiState.value?.togetherDayTitle)
        assertEquals("1000", vm.uiState.value?.togetherDay)
    }
}