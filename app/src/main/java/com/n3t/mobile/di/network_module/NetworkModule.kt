package com.n3t.mobile.di.network_module

import com.n3t.mobile.core.services.NetworkService
import com.n3t.mobile.core.services.NetworkServiceImpl
import com.n3t.mobile.di.network_module.interceptor.AuthenInterceptor
import com.n3t.mobile.di.network_module.interceptor.LoggerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val networkModule = module {
    single { provideGson() }
    single { AuthenInterceptor(get()) }
    single { LoggerInterceptor() }

    singleOf(::NetworkServiceImpl) { bind<NetworkService>() }

    single(qualifier = API_SERVICE_CLIENT) { providesBuildApiServiceHttpClient(get(), get()) }
    single { providesN3TApiService(get(), get(qualifier = API_SERVICE_CLIENT)) }
}

fun provideGson(): Gson {
    return GsonBuilder().create()
}
