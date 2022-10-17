package com.boredream.lovebook.data.source

import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.net.ServiceFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TheDayRepository @Inject constructor(serviceFactory: ServiceFactory) : BaseRepository(serviceFactory) {

    suspend fun getList() = service.getTheDayList()
    suspend fun addTheDay(theDay: TheDay) = service.addTheDay(theDay)
    suspend fun updateTheDay(theDay: TheDay) = service.updateTheDay(theDay, theDay.id ?: "")
    suspend fun deleteTheDay(id: String) = service.deleteTheDay(id)

}