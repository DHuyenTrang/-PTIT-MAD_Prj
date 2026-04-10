package com.n3t.mobile.data.model.authen.verify_otp

import com.google.gson.annotations.SerializedName

data class VerifyOTPRequest(
    @SerializedName("phone") val phone: String,
    @SerializedName("otp_code") val otpCode: String,
    @SerializedName("display_name") val displayName: String? = null,
    @SerializedName("password") val password: String? = null,
)

data class VerifyOTPData(
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: VerifyOTPDataDetail? = null,
)

data class VerifyOTPDataDetail(
    @SerializedName("access_token") val accessToken: String? = null,
    @SerializedName("refresh_token") val refreshToken: String? = null,
    @SerializedName("expire_time") val expireTime: Long? = null,
    @SerializedName("customer_id") val customerId: Int? = null,
    @SerializedName("display_name") val displayName: String? = null,
    @SerializedName("phone") val phone: String? = null,
)
