package com.n3t.mobile.data.model.api_util

data class Failure(
    val errorType: ErrorType? = null,
    // error message
    override val message: String? = null,
    // error code from server
    val errorCode: Int? = null,
//    444: cannot ping to serevr
//    445: cannot create socket connection
//    446: cannot send message to server
//    447: cannot parse response

) : Throwable()

