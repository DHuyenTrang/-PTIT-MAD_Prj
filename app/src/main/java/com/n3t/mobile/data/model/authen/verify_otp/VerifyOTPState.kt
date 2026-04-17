package com.n3t.mobile.data.model.authen.verify_otp

enum class VerifyOTPStatus {
    RESEND,
    COUNT_DOWN,
}

data class VerifyOTPState(
    val otpCodeError: Int? = null,
    val otpCode: String = "",
)

