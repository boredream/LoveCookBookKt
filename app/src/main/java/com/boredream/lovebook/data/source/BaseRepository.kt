package com.boredream.lovebook.data.source

import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.net.ServiceFactory

open class BaseRepository(serviceFactory: ServiceFactory) {

    protected val service: ApiService = serviceFactory.getApiService()

}