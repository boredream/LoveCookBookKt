package com.boredream.lovebook.net

import com.boredream.lovebook.utils.DataStoreUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceCreator {

    private const val HOST = "https://www.papikoala.cn/api/"
    private const val TOKEN_KEY = "token"
    private const val CONNECT_TIMEOUT = 30L
    private const val READ_TIMEOUT = 10L

    private fun create(): Retrofit {
        // okHttpClientBuilder
        val okHttpClientBuilder = OkHttpClient().newBuilder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor {
                // set request token
                val request = it.request()
                val builder = request.newBuilder()

                val token = DataStoreUtils.readStringData(TOKEN_KEY, "")
                if (token.isNotEmpty()) {
                    builder.addHeader(TOKEN_KEY, token)
                }
                it.proceed(builder.build())
            }

        return Retrofit.Builder().apply {
            baseUrl(HOST)
            client(okHttpClientBuilder.build())
            addConverterFactory(GsonConverterFactory.create())
        }.build()
    }

    /**
     * get ServiceApi
     */
    fun <T> create(service: Class<T>): T = create().create(service)

}
