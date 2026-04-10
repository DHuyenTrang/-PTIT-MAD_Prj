package com.n3t.mobile.di.network_module

import org.koin.core.qualifier.named

val API_SERVICE_CLIENT = named("ApiServiceHttpClient")
val TRACKING_SERVICE_CLIENT = named("TrackingServiceHttpClient")
val SEARCH_MEDIA_CLIENT = named("SearchMediaServiceHttpClient")
