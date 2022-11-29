package com.boredream.lovebook.data.dto

/**
 * PageResultDto 是对应接口的单页数据，这里是多页合并的总数据
 */
data class ListResult<T>(
    val hasMore: Boolean,
    val dataList: ArrayList<T>,
)