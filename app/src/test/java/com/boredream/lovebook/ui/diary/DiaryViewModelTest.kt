package com.boredream.lovebook.ui.diary

import com.boredream.lovebook.TestDataConstants
import com.boredream.lovebook.data.repo.DiaryRepository
import com.boredream.lovebook.data.repo.UserRepository
import com.boredream.lovebook.net.ServiceFactory
import com.boredream.lovebook.ui.login.LoginViewModel
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

class DiaryViewModelTest {

    private lateinit var repo: DiaryRepository
    private lateinit var vm: LoginViewModel

    @Before
    fun setUp() {
        val factory = ServiceFactory()
        factory.testToken = TestDataConstants.token
    }

    @Test
    fun getLoginUiState() {

    }

    @Test
    fun login() {
        vm.login()
    }
}