package com.n3t.mobile.data.model.authen.login

import com.google.gson.annotations.SerializedName
data class PasswordRequiredRequest(
    @SerializedName("password")
    val password: String,
    @SerializedName("code")
    val code: String,
)

data class PasswordRequiredResponse(
    @SerializedName("access_token")
    val accessToken: String? = null,
    @SerializedName("refresh_token")
    val refreshToken: String? = null,
)

