package com.boredream.lovebook.ui.login

import com.boredream.lovebook.TestConstants
import com.boredream.lovebook.data.source.UserRepository
import com.boredream.lovebook.net.ServiceFactory
import org.junit.Before
import org.junit.Test

class LoginViewModelTest {

    private lateinit var repo: UserRepository
    private lateinit var vm: LoginViewModel

    @Before
    fun setUp() {
        val factory = ServiceFactory()
        factory.isTest = true
        factory.testToken = TestConstants.token
        repo = UserRepository(factory)
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