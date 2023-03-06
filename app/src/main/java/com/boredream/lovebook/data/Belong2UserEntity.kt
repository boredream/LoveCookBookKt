package com.boredream.lovebook.data

import androidx.room.Ignore
import com.boredream.lovebook.base.BaseEntity

open class Belong2UserEntity : BaseEntity() {

    @Ignore
    open lateinit var user: User

}