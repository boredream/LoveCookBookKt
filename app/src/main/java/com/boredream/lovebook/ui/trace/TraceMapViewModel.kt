package com.boredream.lovebook.ui.trace

import com.boredream.lovebook.base.BaseViewModel
import com.boredream.lovebook.data.repo.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TraceMapViewModel @Inject constructor(
    val repository: LocationRepository
) : BaseViewModel() {

    fun startLocation() {
        repository.startLocation()
    }

}