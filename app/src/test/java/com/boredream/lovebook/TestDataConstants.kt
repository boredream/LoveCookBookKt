package com.boredream.lovebook

import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TraceLocation
import com.boredream.lovebook.data.User
import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.net.ServiceCreator

object TestDataConstants {

    var user: User = createUser()
    const val token =
        "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6NSwiZXhwIjoxNjY5ODcxNzI3fQ.3bKD6UKKq-smx__XbKfHJwnD4pm-sx0DJAo8NFdRpZY"

    fun <T> successResponse(data: T): ResponseEntity<T> {
        return ResponseEntity(data, 0, "success")
    }

    fun createUser(): User {
        val user = User()
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
        return user
    }

    fun getApiService(): ApiService {
        ServiceCreator.tokenFactory = { token }
        return ServiceCreator.create(ApiService::class.java)
    }

    fun getTraceList(): ArrayList<TraceLocation> {
        val traceList = ArrayList<TraceLocation>()
        for (i in 0..10) {
            traceList.add(getStepTraceLocation())
        }
        return traceList
    }

    private var step = 0
    fun getStepTraceLocation(): TraceLocation {
        val extra = step * 0.000001
        val time = System.currentTimeMillis() + step * 2000
        step += 1
        return getTraceLocation(latExtra = extra, time = time)
    }

    fun getTraceLocation(
        latExtra: Double = 0.0,
        lngExtra: Double = 0.0,
        time: Long = System.currentTimeMillis()
    ): TraceLocation {
        return TraceLocation(
            latitude = 31.227792 + latExtra,
            longitude = 121.355379 + lngExtra,
            time = time
        )
    }

}