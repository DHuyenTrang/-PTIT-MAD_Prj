package com.n3t.mobile.data.model.authen.login

import com.google.gson.annotations.SerializedName

data class LoginWithPasswordRequest(
    @SerializedName("phone") val phone: String,
    @SerializedName("password") val password: String,
)

data class LoginWithPasswordResponse(
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: LoginData? = null,
)

data class LoginData(
    @SerializedName("access_token") val accessToken: String? = null,
    @SerializedName("refresh_token") val refreshToken: String? = null,
    @SerializedName("expire_time") val expireTime: Long? = null,
    @SerializedName("customer_id") val customerId: Int? = null,
    @SerializedName("display_name") val displayName: String? = null,
    @SerializedName("phone") val phone: String? = null,
)

data class GetAccountStatus(
    @SerializedName("phone") val phone: String,
)

data class GetAccountStatusResponse(
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: AccountStatusData? = null,
)

data class AccountStatusData(
    @SerializedName("has_password") val hasPassword: Boolean? = null,
    @SerializedName("is_registered") val isRegistered: Boolean? = null,
)

data class PasswordForgotRequest(
    @SerializedName("phone") val phone: String,
)

data class PasswordForgotResponse(
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("message") val message: String? = null,
)

data class PasswordUpdateRequest(
    @SerializedName("old_password") val oldPassword: String? = null,
    @SerializedName("new_password") val newPassword: String,
    @SerializedName("otp_code") val otpCode: String? = null,
    @SerializedName("phone") val phone: String? = null,
)

data class PasswordUpdateResponse(
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("message") val message: String? = null,
)

data class ReSendOtpRequest(
    @SerializedName("phone") val phone: String,
)

data class ReSendOtpResponse(
    @SerializedName("success") val success: Boolean? = null,
    @SerializedName("message") val message: String? = null,
)

data class LoginRequest(
    @SerializedName("phone") val phone: String,
)
