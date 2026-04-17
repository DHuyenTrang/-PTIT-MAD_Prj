package com.n3t.mobile.data.model.api_util

import android.content.Context
import com.n3t.mobile.R

sealed class Either<out F, out S> {
    data class Failure<out F> internal constructor(val value: F) : Either<F, Nothing>() {
        companion object {
            operator fun <F> invoke(f: F): Either<F, Nothing> = Failure(f)
        }
    }

    data class Success<out S> internal constructor(val value: S) : Either<Nothing, S>() {
        companion object {
            operator fun <S> invoke(s: S): Either<Nothing, S> = Success(s)
        }
    }

    companion object {
        fun <S> success(value: S): Either<Nothing, S> = Success(value)
        fun <F> failure(value: F): Either<F, Nothing> = Failure(value)
    }
}


fun <F, S> Either<F, S>.isLeft() = this is Either.Failure
fun <F, S> Either<F, S>.isRight() = this is Either.Success

fun <F, S> Either<F, S>.whenLeftRight(
    left: (F) -> Unit,
    right: (S) -> Unit
) {
    when (this) {
        is Either.Failure -> left(value)
        is Either.Success -> right(value)
    }
}

fun <F, S> Either<F, S>.whenLeft(
    left: (F) -> Unit
) {
    if(isLeft()) {
        left((this as Either.Failure).value)
    }
}

fun <F, S> Either<F, S>.whenRight(
    right: (S) -> Unit
) {
    if(isRight()) {
        right((this as Either.Success).value)
    }
}

fun Failure.getErrorMessage(context: Context): String? {
    return when (this.errorType) {
        ErrorType.LOST_INTERNET -> context.getString(R.string.error_lost_internet)
        ErrorType.SERVER_RESPONSE_ERROR -> null
        ErrorType.SERVER_ERROR -> context.getString(R.string.error_server_message)
        ErrorType.REQUEST_TIMEOUT -> context.getString(R.string.error_request_timeout)
        else -> context.getString(R.string.error_some_thing_wrong)
    }
}

