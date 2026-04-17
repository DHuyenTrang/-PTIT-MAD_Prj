package com.n3t.mobile.di.network_module

import org.koin.core.qualifier.named

val API_SERVICE_CLIENT = named("ApiServiceHttpClient")
val TRACKING_SERVICE_CLIENT = named("TrackingServiceHttpClient")
val SEARCH_MEDIA_CLIENT = named("SearchMediaServiceHttpClient")
val GOOGLE_PLACES_CLIENT = named("GooglePlacesHttpClient")
val GOOGLE_ROUTES_CLIENT = named("GoogleRoutesHttpClient")
val GOONG_CLIENT = named("GoongHttpClient")
val GOONG_RETROFIT = named("GoongRetrofit")
val STATION_SERVICE_CLIENT = named("StationServiceHttpClient")
