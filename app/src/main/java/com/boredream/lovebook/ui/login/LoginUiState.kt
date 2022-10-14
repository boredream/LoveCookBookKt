package com.boredream.lovebook.ui.login

data class LoginUiState(
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val errorTip: String? = null
)