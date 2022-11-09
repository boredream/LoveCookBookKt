package com.boredream.lovebook.base

import java.util.Objects

open class BaseLiveEvent

data class StartActivityLiveEvent<T>(
    val activity: Class<T>,
    val bundle: Map<String, Objects> = emptyMap()
) : BaseLiveEvent()

class FinishSelfActivityLiveEvent : BaseLiveEvent()

data class ToastLiveEvent(
    val toast: String
) : BaseLiveEvent()
