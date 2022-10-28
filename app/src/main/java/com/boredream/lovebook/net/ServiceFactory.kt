package com.boredream.lovebook.net

import com.blankj.utilcode.util.StringUtils
import com.boredream.lovebook.data.constant.DataStoreKey
import com.boredream.lovebook.utils.DataStoreUtils
import javax.inject.Inject

class ServiceFactory @Inject constructor() {

    var testToken = ""

    fun getApiService(): ApiService {
        if(StringUtils.isEmpty(testToken)) {
            // TODO: 是否有更好的方法，把test和正常的分开？
            ServiceCreator.tokenFactory = { DataStoreUtils.readStringData(DataStoreKey.TOKEN, "") }
        } else {
            ServiceCreator.tokenFactory = { testToken }
        }
        return ServiceCreator.create(ApiService::class.java)
    }
}