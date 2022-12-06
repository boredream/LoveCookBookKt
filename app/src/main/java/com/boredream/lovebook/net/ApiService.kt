package com.boredream.lovebook.net

import com.boredream.lovebook.data.*
import com.boredream.lovebook.data.dto.FileUploadPolicyDTO
import com.boredream.lovebook.data.dto.LoginDto
import com.boredream.lovebook.data.dto.PageResultDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface ApiService {

    // ******** common ********
    @GET("file/getUploadPolicy")
    suspend fun getUploadPolicy(): ResponseEntity<FileUploadPolicyDTO>

    /**
     * 七牛
     * resource_key	    否	资源名，必须是UTF-8编码。如果上传凭证中 scope 指定为 <bucket>:<key>， 则该字段也必须指定，并且与上传凭证中的 key 一致，否则会报403错误。如果表单没有指定 key，可以使用上传策略saveKey字段所指定魔法变量生成 Key，如果没有模板，则使用 Hash 值作为 Key。
     * custom_name	    否	自定义变量的名字，不限个数。
     * custom_value	    否	自定义变量的值。
     * upload_token	    是	上传凭证，位于 token 消息中。
     * crc32	        否	上传内容的 CRC32 校验码。如果指定此值，则七牛服务器会使用此值进行内容检验。
     * accept	        否	当 HTTP 请求指定 accept 头部时，七牛会返回 Content-Type 头部值。该值用于兼容低版本 IE 浏览器行为。低版本 IE 浏览器在表单上传时，返回 application/json 表示下载，返回 text/plain 才会显示返回内容。
     * fileName	        是	原文件名。对于没有文件名的情况，建议填入随机生成的纯文本字符串。本参数的值将作为魔法变量$(fname)的值使用。
     * fileBinaryData	是	上传文件的完整内容。
     * x-qn-meta	    否	自定义元数据，可同时自定义多个元数据。
     */
    @Multipart
    @POST
    suspend fun uploadFile7niu(
        @Url url: String,
        @Part file: MultipartBody.Part,
        @Part("upload_token") upload_token: RequestBody,
        @Part("fileName") fileName: RequestBody
    ): ResponseEntity<String>


    // ******** user ********

    @POST("user/login")
    suspend fun login(
        @Body dto: LoginDto,
    ): ResponseEntity<String>

    @GET("user/info")
    suspend fun getUserInfo(): ResponseEntity<User>

    @PUT("user/{id}")
    suspend fun updateUserInfo(
        @Path("id") id: String,
        @Body dto: User,
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
        @Path("id") id: String,
        @Body dto: TheDay,
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
        @Path("id") id: String,
        @Body dto: Diary,
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
        @Path("id") id: String,
        @Body dto: TodoGroup,
    ): ResponseEntity<Boolean>

    @DELETE("todo_group/{id}")
    suspend fun deleteTodoGroup(
        @Path("id") id: String,
    ): ResponseEntity<Boolean>

    // ******** todo_info ********

    @GET("todo/{id}")
    suspend fun getTodoList(
        @Path("id") groupId: String,
    ): ResponseEntity<List<Todo>>

    @POST("todo")
    suspend fun addTodo(
        @Body dto: Todo,
    ): ResponseEntity<Boolean>

    @PUT("todo/{id}")
    suspend fun updateTodo(
        @Path("id") id: String,
        @Body dto: Todo,
    ): ResponseEntity<Boolean>

    @DELETE("todo/{id}")
    suspend fun deleteTodo(
        @Path("id") id: String,
    ): ResponseEntity<Boolean>

    // ******** trace record ********

    @GET("trace_record/{id}")
    suspend fun getTraceRecordList(
        @Path("id") groupId: String,
    ): ResponseEntity<List<TraceRecord>>

    @POST("trace_record")
    suspend fun addTraceRecord(
        @Body dto: TraceRecord,
    ): ResponseEntity<Boolean>

    @PUT("trace_record/{id}")
    suspend fun updateTraceRecord(
        @Path("id") id: String,
        @Body dto: TraceRecord,
    ): ResponseEntity<Boolean>

    @DELETE("trace_record/{id}")
    suspend fun deleteTraceRecord(
        @Path("id") id: String,
    ): ResponseEntity<Boolean>


}