package com.n3t.mobile.data.model.api_util

data class Failure(
    val errorType: ErrorType? = null,
    override val message: String? = null,
    val errorCode: Int? = null,
) : Throwable()
