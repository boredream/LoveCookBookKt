package com.boredream.lovebook.ui.login

import com.boredream.lovebook.TestDataConstants
import com.boredream.lovebook.data.repo.UserRepository
import com.boredream.lovebook.net.ServiceFactory
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {

    @MockK
    private lateinit var repo: UserRepository
    private lateinit var vm: LoginViewModel

    @Before
    fun setUp() {
        val factory = ServiceFactory()
        factory.testToken = TestDataConstants.token
        vm = LoginViewModel(repo)
    }

    @Test
    fun getLoginUiState() {

    }

    @Test
    fun login() {
        vm.login()
    }
}