package com.boredream.lovebook.data.repo

import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.net.ServiceFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TheDayRepository @Inject constructor(serviceFactory: ServiceFactory) : BaseRepository(serviceFactory) {

    suspend fun getList() = service.getTheDayList()
    suspend fun add(data: TheDay) = service.addTheDay(data)
    suspend fun update(data: TheDay) = service.updateTheDay(data, data.id!!)
    suspend fun delete(id: String) = service.deleteTheDay(id)

}