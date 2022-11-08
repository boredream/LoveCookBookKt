package com.boredream.lovebook.net

import com.boredream.lovebook.data.*
import com.boredream.lovebook.data.dto.LoginDto
import com.boredream.lovebook.data.dto.PageResultDto
import retrofit2.http.*

interface ApiService {

    // ******** user ********

    @POST("user/login")
    suspend fun login(
        @Body dto: LoginDto,
    ): ResponseEntity<String>

    @GET("user/info")
    suspend fun getUserInfo(): ResponseEntity<User>

    @PUT("user/{id}")
    suspend fun updateUserInfo(
        @Body dto: User,
        @Path("id") id: String,
    ): ResponseEntity<Boolean>


    // ******** the day ********

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


    // ******** diary ********

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

    // ******** todo_group ********

    @GET("todo_group/only_group")
    suspend fun getTodoGroupList(
    ): ResponseEntity<List<TodoGroup>>

    @POST("todo_group")
    suspend fun addTodoGroup(
        @Body dto: TodoGroup,
    ): ResponseEntity<Boolean>

    @PUT("todo_group/{id}")
    suspend fun updateTodoGroup(
        @Body dto: TodoGroup,
        @Path("id") id: String,
    ): ResponseEntity<Boolean>

    @DELETE("todo_group/{id}")
    suspend fun deleteTodoGroup(
        @Path("id") id: String,
    ): ResponseEntity<Boolean>

    // ******** todo_info ********

    @GET("todo/{id}")
    suspend fun getTodoList(
        @Path("id") groupId: String,
    ): ResponseEntity<PageResultDto<TodoGroup>>

    @POST("todo")
    suspend fun addTodo(
        @Body dto: Todo,
    ): ResponseEntity<Boolean>

    @PUT("todo/{id}")
    suspend fun updateTodo(
        @Body dto: Todo,
        @Path("id") id: String,
    ): ResponseEntity<Boolean>

    @DELETE("todo/{id}")
    suspend fun deleteTodo(
        @Path("id") id: String,
    ): ResponseEntity<Boolean>


}