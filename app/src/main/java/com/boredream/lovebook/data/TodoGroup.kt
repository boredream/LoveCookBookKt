package com.boredream.lovebook.data

data class TodoGroup(
    var name: String,
    var progress: Int = 0,
    var total: Int = 0,
) : Belong2UserEntity()
