package com.n3t.mobile.presentation.authen.verify_otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.api_util.Failure
import com.n3t.mobile.data.model.authen.login.ReSendOtpRequest
import com.n3t.mobile.data.model.authen.login.ReSendOtpResponse
import com.n3t.mobile.data.model.authen.verify_otp.VerifyOTPData
import com.n3t.mobile.data.model.authen.verify_otp.VerifyOTPRequest
import com.n3t.mobile.data.repositories.AuthenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class VerifyOtpViewModel(
    private val authenRepository: AuthenRepository,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _verifyResult = MutableStateFlow<Either<Failure, VerifyOTPData?>?>(null)
    val verifyResult: StateFlow<Either<Failure, VerifyOTPData?>?> = _verifyResult.asStateFlow()

    private val _resendResult = MutableStateFlow<Either<Failure, ReSendOtpResponse?>?>(null)
    val resendResult: StateFlow<Either<Failure, ReSendOtpResponse?>?> = _resendResult.asStateFlow()

    fun verifyOtp(phone: String, otpCode: String, displayName: String? = null, password: String? = null) =
        viewModelScope.launch {
            _isLoading.value = true
            _verifyResult.value = authenRepository.verifyOTP(
                VerifyOTPRequest(phone, otpCode, displayName, password)
            )
            _isLoading.value = false
        }

    fun resendOtp(phone: String) = viewModelScope.launch {
        _isLoading.value = true
        _resendResult.value = authenRepository.resendOtp(ReSendOtpRequest(phone))
        _isLoading.value = false
    }
}
