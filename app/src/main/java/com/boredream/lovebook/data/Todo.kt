package com.boredream.lovebook.data

data class Todo(
    var todoGroupId: String,
    var done: Boolean = false,
    var name: String,
    var doneDate: String? = null,
    var detail: String? = null,
    var images: String? = null,
) : Belong2UserEntity()
