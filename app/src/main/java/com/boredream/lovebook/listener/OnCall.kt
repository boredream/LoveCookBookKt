package com.boredream.lovebook.listener

interface OnCall<T> {
    fun call(t: T)
}