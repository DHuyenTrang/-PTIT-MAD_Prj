package com.n3t.mobile.data.model.authen

import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("success") val success: Boolean? = null,
)
