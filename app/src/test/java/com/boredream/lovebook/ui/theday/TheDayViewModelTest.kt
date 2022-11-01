package com.boredream.lovebook.ui.theday

import com.boredream.lovebook.TestDataConstants
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.repo.TheDayRepository
import com.boredream.lovebook.data.repo.UserRepository
import com.boredream.lovebook.net.ServiceFactory
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
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
    fun loadTogetherInfo_noCp() {
        val user = TestDataConstants.user
        user.cpUser = null
        every { runBlocking {
            userRepo.getUserInfo()
        } } returns TestDataConstants.successResponse(user)

        vm.loadTogetherInfo()
    }
}