package com.n3t.mobile.view_model.authen.verify_otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.api_util.Failure

import com.n3t.mobile.data.model.authen.login.ReSendOtpRequest
import com.n3t.mobile.data.model.authen.login.ReSendOtpResponse
import com.n3t.mobile.data.model.authen.verify_otp.VerifyOTPData
import com.n3t.mobile.data.model.authen.verify_otp.VerifyOTPRequest
import com.n3t.mobile.data.model.authen.verify_otp.VerifyOTPState
import com.n3t.mobile.data.model.authen.verify_otp.VerifyOTPStatus
import com.n3t.mobile.data.repositories.AuthenRepository

import kotlinx.coroutines.launch

class VerifyOtpViewModel(
    private val authenRepository: AuthenRepository
) : ViewModel()  {
    private val _verifyOtpForm = MutableLiveData<VerifyOTPState>()
    val verifyOtpFormState: LiveData<VerifyOTPState> = _verifyOtpForm

    private val _verifyOtpStatus = MutableLiveData(VerifyOTPStatus.COUNT_DOWN)
    val verifyOtpStatus: LiveData<VerifyOTPStatus> = _verifyOtpStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _verifyOtpResult = MutableLiveData<Either<Failure, VerifyOTPData?>>()
    val verifyOtpResult: LiveData<Either<Failure, VerifyOTPData?>> = _verifyOtpResult

    fun setOtpCode(otpCode: String) {
        _verifyOtpForm.value = VerifyOTPState(otpCode = otpCode)
    }

    fun toggleVerifyOtpStatus() {
        _verifyOtpStatus.value = if (_verifyOtpStatus.value == VerifyOTPStatus.COUNT_DOWN) {

            VerifyOTPStatus.RESEND
        } else {

            VerifyOTPStatus.COUNT_DOWN
        }
    }



    fun resendOtp(data: ReSendOtpRequest, callback: (Either<Failure, ReSendOtpResponse?>) -> Unit) = viewModelScope.launch {
        _isLoading.value = true
        val response = authenRepository.resendOtp(data)
        _verifyOtpStatus.value = VerifyOTPStatus.COUNT_DOWN
        _isLoading.value = false

        callback(response)
    }

    fun verifyOtp(data: VerifyOTPRequest) = viewModelScope.launch {
        _isLoading.value = true
        _verifyOtpResult.value = authenRepository.register(data)
        _isLoading.value = false
    }
}

