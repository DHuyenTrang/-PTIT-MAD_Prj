package com.n3t.mobile.di

import com.n3t.mobile.presentation.authen.login.LoginViewModel
import com.n3t.mobile.presentation.authen.register.RegisterViewModel
import com.n3t.mobile.presentation.authen.forgot_password.ForgotPasswordViewModel
import com.n3t.mobile.presentation.authen.verify_otp.VerifyOtpViewModel
import com.n3t.mobile.presentation.license_plate.LicensePlateViewModel
import com.n3t.mobile.presentation.bot.BotViewModel
import com.n3t.mobile.presentation.search.SearchViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { ForgotPasswordViewModel(get()) }
    viewModel { VerifyOtpViewModel(get()) }
    viewModel { LicensePlateViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    singleOf(::BotViewModel)
}
