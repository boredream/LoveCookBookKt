package com.boredream.lovebook.data.dto

data class PageResultDto<T>(
    val current: Int,
    val records: List<T>,
)