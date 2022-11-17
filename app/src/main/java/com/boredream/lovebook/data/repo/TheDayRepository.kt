package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseRepository
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.net.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TheDayRepository @Inject constructor(private val service: ApiService) :
    BaseRepository(service) {

    suspend fun getList() = service.getTheDayList()
    suspend fun add(data: TheDay) = service.addTheDay(data)
    suspend fun update(data: TheDay) = service.updateTheDay(data.id!!, data)
    suspend fun delete(id: String) = service.deleteTheDay(id)

}