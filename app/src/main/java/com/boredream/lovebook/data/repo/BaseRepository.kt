package com.boredream.lovebook.data.repo

import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.net.ServiceFactory

open class BaseRepository(serviceFactory: ServiceFactory) {

    protected val service: ApiService = serviceFactory.getApiService()

}