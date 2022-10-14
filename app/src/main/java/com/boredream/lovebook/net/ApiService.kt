package com.boredream.lovebook.net

import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.PageResult
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.User
import com.boredream.lovebook.data.dto.LoginDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("user/login")
    suspend fun login(
        @Body dto: LoginDto
    ): ResponseEntity<String>

    @GET("user/info")
    suspend fun getUserInfo(): ResponseEntity<User>

    @GET("the_day/page")
    suspend fun getTheDayList(
        @Query("page") page: Int,
        @Query("size") size: Int = 100
    ): ResponseEntity<PageResult<TheDay>>

}