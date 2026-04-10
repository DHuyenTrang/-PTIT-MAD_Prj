package com.n3t.mobile.di

import com.n3t.mobile.data.repositories.AuthenRepository
import com.n3t.mobile.data.repositories.AuthenRepositoryImpl
import com.n3t.mobile.data.repositories.LicensePlateRepository
import com.n3t.mobile.data.repositories.LicensePlateRepositoryImpl
import com.n3t.mobile.data.repositories.PlaceRepository
import com.n3t.mobile.data.repositories.PlaceRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::AuthenRepositoryImpl) { bind<AuthenRepository>() }
    singleOf(::LicensePlateRepositoryImpl) { bind<LicensePlateRepository>() }
    singleOf(::PlaceRepositoryImpl) { bind<PlaceRepository>() }
}
