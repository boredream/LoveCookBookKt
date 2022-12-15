package com.boredream.lovebook.ui.diary

import com.boredream.lovebook.common.SimpleListViewModel
import com.boredream.lovebook.data.Diary
import com.boredream.lovebook.data.repo.DiaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val repository: DiaryRepository
) : SimpleListViewModel<Diary>() {

    override fun isPageList() = true
    override suspend fun repoDeleteRequest(data: Diary) = repository.delete(data.id!!)
    override suspend fun repoPageListRequest(
        loadMore: Boolean,
        forceRemote: Boolean
    ) = repository.getPageList(forceRemote)

}