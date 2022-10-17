package com.boredream.lovebook.net

import javax.inject.Inject

class ServiceFactory @Inject constructor(private val forTest: Boolean = false) {

    fun create(): ApiService {
        val apiService: ApiService = if(forTest) {
            ServiceCreator4Test.create(ApiService::class.java, "")
        } else {
            ServiceCreator.create(ApiService::class.java)
        }
        return apiService
    }
}