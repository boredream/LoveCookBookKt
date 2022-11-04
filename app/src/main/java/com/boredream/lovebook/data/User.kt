package com.boredream.lovebook.data

import com.boredream.lovebook.base.BaseEntity

class User : BaseEntity() {
    var username: String? = null
    var nickname: String? = null
    var gender: String? = null
    var birthday: String? = null
    var avatar: String? = null
    var cpTogetherDate: String? = null
    var cpUser: User? = null
}