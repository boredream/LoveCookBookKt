package com.boredream.lovebook.net

import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.PageResult
import com.boredream.lovebook.data.ResponseEntity
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("user/login")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): ResponseEntity<String>

    @GET("the_day/page")
    suspend fun getTheDayList(
        @Query("page") page: Int,
        @Query("size") size: Int = 100
    ): ResponseEntity<PageResult<TheDay>>

}