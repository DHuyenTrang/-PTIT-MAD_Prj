package com.n3t.mobile

import android.app.Application
import com.n3t.mobile.data.api.Environment
import com.n3t.mobile.di.appModule
import com.mapbox.common.MapboxOptions
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class N3TApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Load environment
        val flavor = Environment.loadFlavor(applicationContext)
        Environment.setFlavor(flavor)

        // Init Mapbox
        MapboxOptions.accessToken = getString(R.string.mapbox_access_token)

        // Init Koin DI
        startKoin {
            androidContext(this@N3TApplication)
            modules(appModule)
        }
    }
}