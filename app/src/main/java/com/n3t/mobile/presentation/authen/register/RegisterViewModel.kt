package com.n3t.mobile.presentation.authen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.api_util.Failure
import com.n3t.mobile.data.model.authen.login.ReSendOtpRequest
import com.n3t.mobile.data.model.authen.login.ReSendOtpResponse
import com.n3t.mobile.data.repositories.AuthenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authenRepository: AuthenRepository,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _sendOtpResult = MutableStateFlow<Either<Failure, ReSendOtpResponse?>?>(null)
    val sendOtpResult: StateFlow<Either<Failure, ReSendOtpResponse?>?> = _sendOtpResult.asStateFlow()

    fun sendOtp(phone: String) = viewModelScope.launch {
        _isLoading.value = true
        _sendOtpResult.value = authenRepository.resendOtp(ReSendOtpRequest(phone))
        _isLoading.value = false
    }
}
