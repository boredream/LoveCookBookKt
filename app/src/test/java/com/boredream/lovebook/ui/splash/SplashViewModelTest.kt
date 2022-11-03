package com.boredream.lovebook.ui.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.boredream.lovebook.MainDispatcherRule
import com.boredream.lovebook.TestDataConstants
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.repo.UserRepository
import com.boredream.lovebook.net.ServiceFactory
import com.boredream.lovebook.ui.login.LoginViewModel
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
import java.net.ConnectException

@OptIn(ExperimentalCoroutinesApi::class)
internal class SplashViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var repo: UserRepository
    private lateinit var vm: SplashViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        vm = SplashViewModel(repo)
    }

    @Test
    fun autoLogin_noToken() = runTest {
        every {
            repo.getLocalToken()
        } returns null

        vm.autoLogin()

        Assert.assertEquals(AutoLoginFail.javaClass, vm.uiState.value?.javaClass)
    }

    @Test
    fun autoLogin_getUserInfoSuccess() = runTest {
        every {
            repo.getLocalToken()
        } returns TestDataConstants.token

        every {
            runBlocking {
                repo.getUserInfo()
            }
        } returns ResponseEntity.success(TestDataConstants.user)

        vm.autoLogin()

        Assert.assertEquals(AutoLoginSuccess.javaClass, vm.uiState.value?.javaClass)
    }

    @Test
    fun autoLogin_getUserInfoNetError() = runTest {
        every {
            repo.getLocalToken()
        } returns TestDataConstants.token
        every {
            runBlocking {
                repo.getUserInfo()
            }
        } throws ConnectException()

        vm.autoLogin()

        Assert.assertEquals(AutoLoginFail.javaClass, vm.uiState.value?.javaClass)
    }

    @Test
    fun autoLogin_getUserInfoFail() = runTest {
        every {
            repo.getLocalToken()
        } returns TestDataConstants.token
        every {
            runBlocking {
                repo.getUserInfo()
            }
        } returns ResponseEntity.unknownError()

        vm.autoLogin()

        Assert.assertEquals(AutoLoginFail.javaClass, vm.uiState.value?.javaClass)
    }

}