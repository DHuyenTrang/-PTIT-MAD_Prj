package com.n3t.mobile.utils.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun <T> LiveData<T>.skipInitialValue(): LiveData<T> {
    val result = MediatorLiveData<T>()
    var firstTime = true
    result.addSource(this) { value ->
        if (firstTime) {
            firstTime = false
            return@addSource
        }
        result.value = value
    }
    return result
}
