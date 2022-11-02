package com.boredream.lovebook.ui

open class BaseLiveEvent

data class StartActivityLiveEvent<T>(
    val activity: Class<T>
) : BaseLiveEvent()

class FinishSelfActivityLiveEvent : BaseLiveEvent()

data class ToastLiveEvent(
    val toast: String
) : BaseLiveEvent()
