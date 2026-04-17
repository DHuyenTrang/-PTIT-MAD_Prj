package com.n3t.mobile.view_model.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.repositories.AuthenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SettingUIState {
    object Idle : SettingUIState()
    object Loading : SettingUIState()
    object LogoutSuccess : SettingUIState()
    data class Error(val message: String) : SettingUIState()
}

class SettingViewModel(
    private val authenRepository: AuthenRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SettingUIState>(SettingUIState.Idle)
    val uiState: StateFlow<SettingUIState> = _uiState.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            _uiState.value = SettingUIState.Loading
            when (val result = authenRepository.logout()) {
                is Either.Failure -> {
                    _uiState.value = SettingUIState.Error("Đăng xuất thất bại. Vui lòng thử lại.")
                }
                is Either.Success -> {
                    _uiState.value = SettingUIState.LogoutSuccess
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = SettingUIState.Idle
    }
}
