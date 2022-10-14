package com.boredream.lovebook.data.source

import com.boredream.lovebook.net.ApiService
import com.boredream.lovebook.net.ServiceCreator
import javax.inject.Inject

open class BaseRepository {

    protected val service: ApiService = ServiceCreator.create(ApiService::class.java)

}