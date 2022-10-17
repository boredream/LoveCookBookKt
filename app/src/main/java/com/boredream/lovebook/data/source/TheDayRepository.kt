package com.boredream.lovebook.data.source

import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.dto.PageResultDto
import com.boredream.lovebook.net.ServiceFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TheDayRepository @Inject constructor(serviceFactory: ServiceFactory) : BaseRepository(serviceFactory) {

    /**
     * 登录
     */
    suspend fun getList(): ResponseEntity<PageResultDto<TheDay>> {
        return service.getTheDayList()
    }

}