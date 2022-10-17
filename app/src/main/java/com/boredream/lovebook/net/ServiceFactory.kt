package com.boredream.lovebook.net

import javax.inject.Inject

class ServiceFactory @Inject constructor() {

    var isTest = false
    var testToken = ""

    fun create(): ApiService {
        val apiService: ApiService = if(isTest) {
            ServiceCreator4Test.create(ApiService::class.java, testToken)
        } else {
            ServiceCreator.create(ApiService::class.java)
        }
        return apiService
    }
}