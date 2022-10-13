package com.boredream.lovebook.data

data class PageResult<T>(
    val current: Long,
    val size: Long,
    val total: Long,
    val pages: Long,
    val records: List<T>
)