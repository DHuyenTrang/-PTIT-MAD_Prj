package com.n3t.mobile.presentation.authen.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.api_util.Failure
import com.n3t.mobile.data.model.authen.login.PasswordForgotRequest
import com.n3t.mobile.data.model.authen.login.PasswordForgotResponse
import com.n3t.mobile.data.model.authen.login.PasswordUpdateRequest
import com.n3t.mobile.data.model.authen.login.PasswordUpdateResponse
import com.n3t.mobile.data.repositories.AuthenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val authenRepository: AuthenRepository,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _forgotPasswordResult = MutableStateFlow<Either<Failure, PasswordForgotResponse?>?>(null)
    val forgotPasswordResult: StateFlow<Either<Failure, PasswordForgotResponse?>?> = _forgotPasswordResult.asStateFlow()

    private val _updatePasswordResult = MutableStateFlow<Either<Failure, PasswordUpdateResponse?>?>(null)
    val updatePasswordResult: StateFlow<Either<Failure, PasswordUpdateResponse?>?> = _updatePasswordResult.asStateFlow()

    fun forgotPassword(phone: String) = viewModelScope.launch {
        _isLoading.value = true
        _forgotPasswordResult.value = authenRepository.forgotPassword(PasswordForgotRequest(phone))
        _isLoading.value = false
    }

    fun updatePassword(newPassword: String, otpCode: String, phone: String) = viewModelScope.launch {
        _isLoading.value = true
        _updatePasswordResult.value = authenRepository.updatePassword(
            PasswordUpdateRequest(newPassword = newPassword, otpCode = otpCode, phone = phone)
        )
        _isLoading.value = false
    }
}
