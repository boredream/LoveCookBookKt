package com.boredream.lovebook.data

data class ResponseEntity<T>(
    val data: T?,
    val code: Int,
    val msg: String
) {
    fun isSuccess() = code == 0

    // TODO: 有更好的写法吗？
    fun getSuccessData() = data!!

    companion object {
        fun <T> notExistError() : ResponseEntity<T> {
            return ResponseEntity(null, 404, "目标不存在")
        }
    }
}
