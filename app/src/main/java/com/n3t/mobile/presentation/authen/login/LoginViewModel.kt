package com.n3t.mobile.presentation.authen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.api_util.Failure
import com.n3t.mobile.data.model.authen.login.GetAccountStatusResponse
import com.n3t.mobile.data.model.authen.login.LoginWithPasswordRequest
import com.n3t.mobile.data.model.authen.login.LoginWithPasswordResponse
import com.n3t.mobile.data.repositories.AuthenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authenRepository: AuthenRepository,
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _checkPhoneExist = MutableStateFlow<Either<Failure, GetAccountStatusResponse>?>(null)
    val checkPhoneExist: StateFlow<Either<Failure, GetAccountStatusResponse>?> = _checkPhoneExist.asStateFlow()

    private val _loginResult = MutableStateFlow<Either<Failure, LoginWithPasswordResponse?>?>(null)
    val loginResult: StateFlow<Either<Failure, LoginWithPasswordResponse?>?> = _loginResult.asStateFlow()

    fun checkPhoneExist(phone: String) = viewModelScope.launch {
        _isLoading.value = true
        _checkPhoneExist.value = authenRepository.checkPhoneExist(phone)
        _isLoading.value = false
    }

    fun loginWithPassword(phone: String, password: String) = viewModelScope.launch {
        _isLoading.value = true
        _loginResult.value = authenRepository.loginWithPassword(
            LoginWithPasswordRequest(phone, password)
        )
        _isLoading.value = false
    }
}
