package com.boredream.lovebook.ui.theday

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.TestDataConstants
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.dto.PageResultDto
import com.boredream.lovebook.data.repo.TheDayRepository
import com.boredream.lovebook.data.repo.UserRepository
import com.boredream.lovebook.net.ServiceFactory
import com.boredream.lovebook.utils.MockUtils
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*


@OptIn(ExperimentalCoroutinesApi::class)
class TheDayViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

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
    fun loadTheDayList() = runTest {
        every {
            runBlocking {
                repo.getList()
            }
        } returns ResponseEntity.success(MockUtils.mockPageResult(TheDay::class.java))

        vm.loadTheDayList()
        assertNotEquals(0, vm.dataList.value?.size)
    }

    @Test
    fun setTogetherDay() = runTest {
        val today = Calendar.getInstance()
        today.add(Calendar.DAY_OF_YEAR, -2)
        val togetherDate = TimeUtils.date2String(Date(today.timeInMillis), "yyyy-MM-dd")

        val user = TestDataConstants.createUser()
        user.cpTogetherDate = togetherDate
        every {
            runBlocking {
                userRepo.updateTogetherDay(any())
            }
        } returns ResponseEntity.success(true)
        every {
            userRepo.getLocalUser()
        } returns user

        // TODO: 提取成rule？
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        vm.setTogetherDay(togetherDate)
        assertNotNull(vm.uiState.value)
        assertEquals("我们已恋爱", vm.uiState.value?.togetherDayTitle)
        assertEquals("2", vm.uiState.value?.togetherDay)
    }

}