package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseRequestFileRepository
import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.net.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryRepository @Inject constructor(private val service: ApiService) :
    BaseRequestFileRepository<Diary>(service) {

    suspend fun getPageList(loadMore: Boolean, forceRemote: Boolean = false) =
        getPageList(forceRemote, loadMore = loadMore) {
            service.getDiaryList(it)
        }

    suspend fun add(data: Diary) = commitWithFile(data, request = service::addDiary)
    suspend fun update(data: Diary) = commitWithFile(data) { service.updateDiary(data.id!!, data) }
    suspend fun delete(id: String) = commitDelete(id) { service.deleteDiary(id) }

}

