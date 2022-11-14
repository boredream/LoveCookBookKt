package com.boredream.lovebook.data

data class Diary(
    var content: String,
    var diaryDate: String,
    var images: String? = null,
) : Belong2UserEntity()