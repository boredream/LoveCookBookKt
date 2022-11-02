package com.boredream.lovebook.data.repo

import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.net.ServiceFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryRepository @Inject constructor(serviceFactory: ServiceFactory) : BaseRepository(serviceFactory) {

    suspend fun getList(page: Int) = service.getDiaryList(page)
    suspend fun add(data: Diary) = service.addDiary(data)
    suspend fun update(data: Diary) = service.updateDiary(data, data.id!!)
    suspend fun delete(id: String) = service.deleteDiary(id)

}