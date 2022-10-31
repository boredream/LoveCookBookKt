package com.boredream.lovebook.net

import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.User
import com.boredream.lovebook.data.dto.LoginDto
import com.boredream.lovebook.data.dto.PageResultDto
import retrofit2.http.*

interface ApiService {

    @POST("user/login")
    suspend fun login(
        @Body dto: LoginDto,
    ): ResponseEntity<String>

    @GET("user/info")
    suspend fun getUserInfo(): ResponseEntity<User>

    // the day

    @GET("the_day/page")
    suspend fun getTheDayList(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 100,
    ): ResponseEntity<PageResultDto<TheDay>>

    @POST("the_day")
    suspend fun addTheDay(
        @Body dto: TheDay,
    ): ResponseEntity<Boolean>

    @PUT("the_day/{id}")
    suspend fun updateTheDay(
        @Body dto: TheDay,
        @Path("id") id: String,
    ): ResponseEntity<Boolean>

    @DELETE("the_day/{id}")
    suspend fun deleteTheDay(
        @Path("id") id: String,
    ): ResponseEntity<Boolean>

    // diary
    @GET("diary/page")
    suspend fun getDiaryList(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20,
    ): ResponseEntity<PageResultDto<Diary>>

    @POST("diary")
    suspend fun addDiary(
        @Body dto: Diary,
    ): ResponseEntity<Boolean>

    @PUT("diary/{id}")
    suspend fun updateDiary(
        @Body dto: Diary,
        @Path("id") id: String,
    ): ResponseEntity<Boolean>

    @DELETE("diary/{id}")
    suspend fun deleteDiary(
        @Path("id") id: String,
    ): ResponseEntity<Boolean>


}