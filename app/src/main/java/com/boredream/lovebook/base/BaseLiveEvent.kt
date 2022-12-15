package com.boredream.lovebook.base

open class BaseLiveEvent

data class ToastLiveEvent(
    val toast: String
) : BaseLiveEvent()
