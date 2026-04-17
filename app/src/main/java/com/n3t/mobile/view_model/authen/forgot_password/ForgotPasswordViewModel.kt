package com.n3t.mobile.view_model.authen.forgot_password

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.api_util.Failure
import com.n3t.mobile.data.model.authen.login.LoginWithPasswordRequest
import com.n3t.mobile.data.model.authen.login.LoginWithPasswordResponse
import com.n3t.mobile.data.model.authen.login.PasswordForgotRequest
import com.n3t.mobile.data.model.authen.login.PasswordForgotResponse
import com.n3t.mobile.data.model.authen.login.PasswordRequiredRequest
import com.n3t.mobile.data.model.authen.login.PasswordRequiredResponse
import com.n3t.mobile.data.model.authen.login.PasswordUpdateRequest
import com.n3t.mobile.data.model.authen.login.PasswordUpdateResponse
import com.n3t.mobile.data.repositories.AuthenRepository
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val authenRepository: AuthenRepository,
) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _savePasswordResult = MutableLiveData<Either<Failure, PasswordRequiredResponse?>>()
    val savePasswordResult: LiveData<Either<Failure, PasswordRequiredResponse?>> = _savePasswordResult

    private val _updatePasswordResult = MutableLiveData<Either<Failure, PasswordUpdateResponse?>>()
    val updatePasswordResult: LiveData<Either<Failure, PasswordUpdateResponse?>> = _updatePasswordResult

    fun savePassword(data: PasswordRequiredRequest) = viewModelScope.launch {
        _isLoading.value = true
        _savePasswordResult.value = null
        _isLoading.value = false
    }

    fun updatePassword(data: PasswordUpdateRequest) = viewModelScope.launch {
        _isLoading.value = true
        _updatePasswordResult.value = authenRepository.updatePassword(data)
        _isLoading.value = false
    }

}

