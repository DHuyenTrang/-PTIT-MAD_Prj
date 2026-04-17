package com.n3t.mobile.di.network_module

import com.n3t.mobile.data.api.Environment
import com.n3t.mobile.data.datasources.remote.GoongApiService
import com.n3t.mobile.data.datasources.remote.N3TApiService
import com.n3t.mobile.data.datasources.remote.StationApiService
import com.n3t.mobile.di.network_module.interceptor.AuthenInterceptor
import com.n3t.mobile.di.network_module.interceptor.GoogleAndroidAppHeaderInterceptor
import com.n3t.mobile.di.network_module.interceptor.LoggerInterceptor
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun providesN3TApiService(
    factory: Gson,
    client: OkHttpClient,
): N3TApiService {
    return Retrofit.Builder()
        .baseUrl(Environment.instance.baseUrlApi)
        .addConverterFactory(GsonConverterFactory.create(factory))
        .client(client)
        .build()
        .create(N3TApiService::class.java)
}

fun providesBuildApiServiceHttpClient(
    authenInterceptor: AuthenInterceptor,
    loggerInterceptor: LoggerInterceptor,
): OkHttpClient {
    val builder = OkHttpClient.Builder()
        .readTimeout(Environment.instance.maxConnectTimeout, TimeUnit.SECONDS)
        .writeTimeout(Environment.instance.maxConnectTimeout, TimeUnit.SECONDS)
        .connectTimeout(Environment.instance.maxConnectTimeout, TimeUnit.SECONDS)
        .callTimeout(Environment.instance.maxConnectTimeout, TimeUnit.SECONDS)
    builder.addInterceptor(authenInterceptor)
    builder.addInterceptor(loggerInterceptor)
    return builder.build()
}

fun providesBuildGoogleHttpClient(
    googleAndroidAppHeaderInterceptor: GoogleAndroidAppHeaderInterceptor,
    loggerInterceptor: LoggerInterceptor,
): OkHttpClient {
    return OkHttpClient.Builder()
        .readTimeout(Environment.instance.maxConnectTimeout, TimeUnit.SECONDS)
        .writeTimeout(Environment.instance.maxConnectTimeout, TimeUnit.SECONDS)
        .connectTimeout(Environment.instance.maxConnectTimeout, TimeUnit.SECONDS)
        .callTimeout(Environment.instance.maxConnectTimeout, TimeUnit.SECONDS)
        .addInterceptor(googleAndroidAppHeaderInterceptor)
        .addInterceptor(loggerInterceptor)
        .build()
}

fun providesGoongRetrofit(
    factory: Gson,
    client: OkHttpClient,
): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://rsapi.goong.io/")
        .addConverterFactory(GsonConverterFactory.create(factory))
        .client(client)
        .build()
}

fun providesGoongApiService(retrofit: Retrofit): GoongApiService {
    return retrofit.create(GoongApiService::class.java)
}

fun providesStationApiService(
    factory: Gson,
    client: OkHttpClient,
): StationApiService {
    return Retrofit.Builder()
        .baseUrl("http://192.168.2.15:8000/")
        .addConverterFactory(GsonConverterFactory.create(factory))
        .client(client)
        .build()
        .create(StationApiService::class.java)
}
