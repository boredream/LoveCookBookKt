package com.boredream.lovebook.data.dto

data class PageResultDto<T>(
    val current: Int,
    val size: Long,
    val total: Long,
    val pages: Int,
    val records: List<T>,
)