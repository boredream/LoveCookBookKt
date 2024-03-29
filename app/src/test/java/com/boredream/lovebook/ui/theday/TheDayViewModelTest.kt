package com.boredream.lovebook.ui.theday

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.blankj.utilcode.util.TimeUtils
import com.boredream.lovebook.MainDispatcherRule
import com.boredream.lovebook.TestDataConstants
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.repo.TheDayRepository
import com.boredream.lovebook.data.repo.UserRepository
import com.boredream.lovebook.ui.todogroup.LoadListSuccess
import com.boredream.lovebook.utils.MockUtils
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList


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

        assertNotNull(vm.headerUiState.value)
        assertEquals("点我设置", vm.headerUiState.value?.togetherDayTitle)
        assertEquals("0", vm.headerUiState.value?.togetherDay)
    }

    @Test
    fun loadTogetherInfo_hasTogetherDay() {
        val today = Calendar.getInstance()
        today.add(Calendar.DAY_OF_YEAR, -2)
        val togetherDate = TimeUtils.date2String(Date(today.timeInMillis), "yyyy-MM-dd")

        val user = TestDataConstants.createUser()
        user.cpTogetherDate = togetherDate

        every {
            userRepo.getLocalUser()
        } returns user

        vm.loadTogetherInfo()

        assertNotNull(vm.headerUiState.value)
        assertEquals("我们已恋爱", vm.headerUiState.value?.togetherDayTitle)
        assertEquals("2", vm.headerUiState.value?.togetherDay)
    }

    @Test
    fun loadTheDayList() = runTest {
        every {
            runBlocking {
                repo.getList()
            }
        } returns ResponseEntity.success(ArrayList(MockUtils.mockList(TheDay::class.java)))

        vm.refresh(loadMore = false, handlePullDownDown = false)
        assertNotNull(vm.refreshListVMCompose.dataListUiState.value)
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

        vm.setTogetherDay(togetherDate)
        assertNotNull(vm.headerUiState.value)
        assertEquals("我们已恋爱", vm.headerUiState.value?.togetherDayTitle)
        assertEquals("2", vm.headerUiState.value?.togetherDay)
    }

}