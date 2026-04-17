package com.n3t.mobile.view_model.authen.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.api_util.Failure

import com.n3t.mobile.data.repositories.AuthenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class RegisterUIState {
    object Idle : RegisterUIState()
    object Loading : RegisterUIState()
    data class EmailExistsError(val message: String) : RegisterUIState()
    data class Error(val failure: Failure) : RegisterUIState()
    data class Success(val email: String) : RegisterUIState()
}

class RegisterViewModel(
    private val authenRepository: AuthenRepository,
): ViewModel() {
    
    private val _uiState = MutableStateFlow<RegisterUIState>(RegisterUIState.Idle)
    val uiState: StateFlow<RegisterUIState> = _uiState.asStateFlow()

    fun checkEmail(email: String) {
        viewModelScope.launch {
            _uiState.value = RegisterUIState.Loading
            when (val result = authenRepository.checkEmailExist(email)) {
                is Either.Failure -> {
                    _uiState.value = RegisterUIState.Error(result.value)
                }
                is Either.Success -> {
                    val response = result.value
                    if (response.success == true) {
                        val isRegistered = response.data?.isRegistered ?: false
                        if (isRegistered) {
                            _uiState.value = RegisterUIState.EmailExistsError("Email đã tồn tại, vui lòng đăng nhập.")
                        } else {
                            _uiState.value = RegisterUIState.Success(email)
                        }
                    } else {
                        _uiState.value = RegisterUIState.EmailExistsError(response.message ?: "Lỗi kiểm tra email.")
                    }
                }
            }
        }
    }
}
