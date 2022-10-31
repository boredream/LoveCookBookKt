package com.boredream.lovebook.data

data class ResponseEntity<T>(
    val data: T,
    val code: Int,
    val msg: String
) {
    fun isSuccess() = code == 0
}