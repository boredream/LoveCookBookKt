package com.boredream.lovebook.net

import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.User
import com.boredream.lovebook.data.dto.LoginDto
import com.boredream.lovebook.data.dto.PageResultDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("user/login")
    suspend fun login(
        @Body dto: LoginDto,
    ): ResponseEntity<String>

    @GET("user/info")
    suspend fun getUserInfo(): ResponseEntity<User>

    @GET("the_day/page")
    suspend fun getTheDayList(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 100,
        @Query("queryDate") queryDate: String,
    ): ResponseEntity<PageResultDto<TheDay>>

}