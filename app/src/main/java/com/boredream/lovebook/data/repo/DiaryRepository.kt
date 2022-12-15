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

    // TODO: 传递进去一个带参数的方法，实际的方法参数是外部传入的值？还是内部invoke传入的值？
    suspend fun add(data: Diary) = commitWithFile(originData = data) { service.addDiary(data) }
    suspend fun update(data: Diary) = commitWithFile(originData = data) { service.updateDiary(data.id!!, data) }
    suspend fun delete(id: String) = commit { service.deleteDiary(id) }

}

