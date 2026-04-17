package com.n3t.mobile.di

import com.n3t.mobile.view_model.authen.forgot_password.ForgotPasswordViewModel
import com.n3t.mobile.view_model.authen.login.LoginViewModel
import com.n3t.mobile.view_model.authen.register.RegisterViewModel
import com.n3t.mobile.view_model.authen.verify_otp.VerifyOtpViewModel
import com.n3t.mobile.view_model.map.MapViewModel
import com.n3t.mobile.view_model.navigation.NavigationViewModel
import com.n3t.mobile.view_model.search.DetailPlaceViewModel
import com.n3t.mobile.view_model.search.RoutingViewModel
import com.n3t.mobile.view_model.search.SearchViewModel
import com.n3t.mobile.view_model.setting.SettingViewModel
import com.n3t.mobile.ui.splash_screen.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { ForgotPasswordViewModel(get()) }
    viewModel { VerifyOtpViewModel(get()) }
    viewModel { SplashViewModel(get()) }
    viewModel { MapViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { DetailPlaceViewModel(get()) }
    viewModel { RoutingViewModel(get()) }
    viewModel { NavigationViewModel() }
    viewModel { SettingViewModel(get()) }
}

