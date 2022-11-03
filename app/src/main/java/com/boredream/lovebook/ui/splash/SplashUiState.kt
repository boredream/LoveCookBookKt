package com.boredream.lovebook.ui.splash

sealed class SplashUiState
object AutoLoginSuccess : SplashUiState()
object AutoLoginFail : SplashUiState()