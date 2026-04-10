package com.n3t.mobile.di

import com.n3t.mobile.data.datasources.local.AppStore
import com.n3t.mobile.di.network_module.networkModule
import org.koin.dsl.module

val appModule = module {
    single { AppStore(get()) }
    includes(networkModule)
    includes(repositoryModule)
    includes(viewModelModule)
}
