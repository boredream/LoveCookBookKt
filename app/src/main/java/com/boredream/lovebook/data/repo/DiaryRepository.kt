package com.boredream.lovebook.data.repo

import com.boredream.lovebook.base.BaseLoadRepository
import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.net.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryRepository @Inject constructor(private val service: ApiService) : BaseLoadRepository<Diary>(service) {

    suspend fun getPageList(loadMore: Boolean) = getPageList(loadMore = loadMore) { service.getDiaryList(it) }
    suspend fun add(data: Diary) = commit { service.addDiary(data) }
    suspend fun update(data: Diary) = commit { service.updateDiary(data.id!!, data) }
    suspend fun delete(id: String) = commit { service.deleteDiary(id) }

}