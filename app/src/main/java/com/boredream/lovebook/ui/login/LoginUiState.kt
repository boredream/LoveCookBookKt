package com.boredream.lovebook.ui.login

// sealed 闭合类，让所有的子类作为枚举，这样 when(uiState) -> 罗列所有子类后就不用else了
// TODO: 单状态的 object，包含数据的 data class ?
sealed class LoginUiState
data class LoginValidateFail(val reason: String) : LoginUiState()
object LoginSuccess : LoginUiState()
data class LoginFail(val reason: String) : LoginUiState()