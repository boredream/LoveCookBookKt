package com.boredream.lovebook.data.source

import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.data.PageResult
import com.boredream.lovebook.data.ResponseEntity

class TheDayRepository: BaseRepository() {

    suspend fun getTheDayList(page: Int): ResponseEntity<PageResult<TheDay>> {
        return service.getTheDayList(page)
    }

}