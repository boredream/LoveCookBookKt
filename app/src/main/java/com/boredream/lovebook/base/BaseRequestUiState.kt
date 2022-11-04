package com.boredream.lovebook.base

sealed class BaseRequestUiState<T>
data class BaseRequestSuccess<T>(val data: T) : BaseRequestUiState<T>()
data class BaseRequestFail<T>(val reason: String) : BaseRequestUiState<T>()