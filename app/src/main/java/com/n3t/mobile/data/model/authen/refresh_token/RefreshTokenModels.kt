package com.n3t.mobile.data.model.authen.refresh_token

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest(
    @SerializedName("refresh_token") val refreshToken: String,
)

data class RefreshTokenResponse(
    @SerializedName("access_token") val accessToken: String? = null,
    @SerializedName("refresh_token") val refreshToken: String? = null,
    @SerializedName("expire_time") val expireTime: Long? = null,
)
