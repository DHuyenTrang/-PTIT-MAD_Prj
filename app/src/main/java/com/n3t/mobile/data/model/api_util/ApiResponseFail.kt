package com.n3t.mobile.data.model.api_util

import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Response

data class ApiResponseFail(
    @Expose
    @SerializedName("message")
    val message: String? = null,
    @Expose
    @SerializedName("code")
    val code: Int? = null,
)

fun <T> Response<T>.toGofaApiResponseFail(): ApiResponseFail? {
    return try {
        val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        val errorBody = this.errorBody()?.string()
        val errorResponse = gson.fromJson(errorBody, ApiResponseFail::class.java)
        errorResponse
    } catch (e: Exception) {
        null
    }
}

fun <T> Response<T>.toApiResponseFail(): ApiResponseFail? = this.toGofaApiResponseFail()

fun Exception.toApiResponseFail(context: android.content.Context? = null): ApiResponseFail? {
    return ApiResponseFail(this.message, -1)
}

