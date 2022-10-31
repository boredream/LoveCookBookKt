package com.boredream.lovebook

import com.boredream.lovebook.data.User

object TestDataConstants {

    var user: User = User()
    const val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6MiwiZXhwIjoxNjY4NTc5NDE3fQ.IpPwei0Xh1yC-GI3DK_fmiGhxIgKsVMk7JjFS3sz1V4"

    init {
        user.id = "2"
        user.username = "18501683421"
        user.nickname = "papi"
        user.gender = "男"
        user.birthday = "1989-12-21"
        user.avatar = "https://file.papikoala.cn/image1643267709520.jpg"
        user.cpUserId = "1"
        user.cpTogetherDate = "2020-02-05"

        user.cpUser = User()
        user.cpUser?.id = "2"
        user.cpUser?.username = "15021631929"
        user.cpUser?.nickname = "koala"
        user.cpUser?.gender = "女"
        user.cpUser?.birthday = "1990-02-14"
        user.cpUser?.avatar = "https://file.papikoala.cn/image1657166004355.jpg"
        user.cpUser?.cpUserId = "2"
        user.cpUser?.cpTogetherDate = "2020-02-05"
    }
    
}