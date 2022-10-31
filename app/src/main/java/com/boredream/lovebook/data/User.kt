package com.boredream.lovebook.data

class User : BaseEntity() {
    var username: String? = null
    var password: String? = null
    var nickname: String? = null
    var gender: String? = null
    var birthday: String? = null
    var avatar: String? = null
    var cpUserId: String? = null
    var cpTogetherDate: String? = null
    var cpUser: User? = null
}