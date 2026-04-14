package com.n3t.mobile.di

import com.n3t.mobile.view_model.authen.forgot_password.ForgotPasswordViewModel
import com.n3t.mobile.view_model.authen.login.LoginViewModel
import com.n3t.mobile.view_model.authen.register.RegisterViewModel
import com.n3t.mobile.view_model.authen.verify_otp.VerifyOtpViewModel
import com.n3t.mobile.ui.splash_screen.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel() }
    viewModel { RegisterViewModel(get()) }
    viewModel { ForgotPasswordViewModel(get()) }
    viewModel { VerifyOtpViewModel(get()) }
    viewModel { SplashViewModel(get()) }
//    viewModel { LicensePlateViewModel(get()) }
//    viewModel { SearchViewModel(get()) }
}
