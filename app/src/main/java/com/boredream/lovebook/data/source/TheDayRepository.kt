package com.boredream.lovebook.data.source

import com.boredream.lovebook.data.ResponseEntity
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.User
import com.boredream.lovebook.data.dto.LoginDto
import com.boredream.lovebook.data.dto.PageResultDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TheDayRepository @Inject constructor() : BaseRepository() {

    /**
     * 登录
     */
    suspend fun getList(queryDate: String): ResponseEntity<PageResultDto<TheDay>> {
        return service.getTheDayList(queryDate = queryDate)
    }

}