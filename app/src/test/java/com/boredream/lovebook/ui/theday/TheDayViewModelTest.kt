package com.boredream.lovebook.ui.diary

import com.boredream.lovebook.TestDataConstants
import com.boredream.lovebook.data.repo.DiaryRepository
import com.boredream.lovebook.data.repo.TheDayRepository
import com.boredream.lovebook.data.repo.UserRepository
import com.boredream.lovebook.net.ServiceFactory
import com.boredream.lovebook.ui.login.LoginViewModel
import com.boredream.lovebook.ui.theday.TheDayViewModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class TheDayViewModelTest {

    @MockK
    private lateinit var repo: TheDayRepository

    @MockK
    private lateinit var userRepo: UserRepository

    private lateinit var vm: TheDayViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        val factory = ServiceFactory()
        factory.testToken = TestDataConstants.token
        vm = TheDayViewModel(repo, userRepo)
    }

    @Test
    fun loadTogetherInfo() {

    }
}