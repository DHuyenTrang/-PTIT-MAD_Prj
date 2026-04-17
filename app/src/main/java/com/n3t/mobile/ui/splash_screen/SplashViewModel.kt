package com.n3t.mobile.ui.splash_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n3t.mobile.data.repositories.AuthenRepository
import com.n3t.mobile.data.model.api_util.whenLeftRight
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SplashState {
    object Initial : SplashState()
    object GoToDashboard : SplashState()
    object GoToLogin : SplashState()
}

class SplashViewModel(
    private val authenRepository: AuthenRepository
) : ViewModel() {

    private val _splashState = MutableStateFlow<SplashState>(SplashState.Initial)
    val splashState: StateFlow<SplashState> = _splashState.asStateFlow()

    fun checkUserProfile() {
        viewModelScope.launch {
            val result = authenRepository.getUserProfile()
            result.whenLeftRight(
                left = {
                    _splashState.value = SplashState.GoToLogin
                },
                right = {
                    _splashState.value = SplashState.GoToDashboard
                }
            )
        }
    }
}
