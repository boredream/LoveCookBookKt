package com.boredream.lovebook.data.dto

data class PageResultDto<T>(
    val current: Long,
    val size: Long,
    val total: Long,
    val pages: Long,
    val records: List<T>,
)