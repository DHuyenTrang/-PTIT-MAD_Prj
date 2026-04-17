package com.n3t.mobile.view_model.authen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.api_util.Failure
import com.n3t.mobile.data.model.authen.login.LoginWithPasswordRequest
import com.n3t.mobile.data.repositories.AuthenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginUIState {
    object Idle : LoginUIState()
    object Loading : LoginUIState()
    data class Error(val message: String) : LoginUIState()
    object Success : LoginUIState()
}

class LoginViewModel(
    private val authenRepository: AuthenRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUIState>(LoginUIState.Idle)
    val uiState: StateFlow<LoginUIState> = _uiState.asStateFlow()

    fun loginWithPassword(email: String, pass: String) {
        viewModelScope.launch {
            _uiState.value = LoginUIState.Loading
            val request = LoginWithPasswordRequest(email = email, password = pass)
            when (val result = authenRepository.loginWithPassword(request)) {
                is Either.Failure -> {
                    _uiState.value = LoginUIState.Error("Lỗi kết nối. Vui lòng thử lại.")
                }
                is Either.Success -> {
                    if (result.value?.success == true) {
                        _uiState.value = LoginUIState.Success
                    } else {
                        _uiState.value = LoginUIState.Error(result.value?.message ?: "Tài khoản hoặc mật khẩu không chính xác.")
                    }
                }
            }
        }
    }
}
