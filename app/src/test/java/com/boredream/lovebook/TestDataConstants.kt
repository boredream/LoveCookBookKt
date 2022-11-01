package com.boredream.lovebook

import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.User

object TestDataConstants {

    var user: User = User()
    const val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6NSwiZXhwIjoxNjY5ODcxNzI3fQ.3bKD6UKKq-smx__XbKfHJwnD4pm-sx0DJAo8NFdRpZY"

    fun <T> successResponse(data: T) : ResponseEntity<T> {
        return ResponseEntity(data, 0, "success")
    }

    init {
        user.id = "5"
        user.username = "18501683422"
        user.nickname = "papi"
        user.gender = "男"
        user.birthday = "1989-12-21"
        user.avatar = "https://file.papikoala.cn/image1643267709520.jpg"
        user.cpTogetherDate = "2020-02-05"

        user.cpUser = User()
        user.cpUser?.id = "6"
        user.cpUser?.username = "18501683423"
        user.cpUser?.nickname = "koala"
        user.cpUser?.gender = "女"
        user.cpUser?.birthday = "1990-02-14"
        user.cpUser?.avatar = "https://file.papikoala.cn/image1657166004355.jpg"
        user.cpUser?.cpTogetherDate = "2020-02-05"
    }
    
}