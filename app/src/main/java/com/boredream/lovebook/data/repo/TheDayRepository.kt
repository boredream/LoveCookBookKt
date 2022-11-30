package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseLoadRepository
import com.boredream.lovebook.data.TheDay
import com.boredream.lovebook.net.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TheDayRepository @Inject constructor(private val service: ApiService) :
    BaseLoadRepository<TheDay>() {

    suspend fun getList(forceRemote: Boolean = false) =
        getPageList(forceRemote) {
            service.getTheDayList(it)
        }

    suspend fun add(data: TheDay) = service.addTheDay(data)
    suspend fun update(data: TheDay) = service.updateTheDay(data.id!!, data)
    suspend fun delete(id: String) = service.deleteTheDay(id)

}