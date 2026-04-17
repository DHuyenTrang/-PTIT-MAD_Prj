package com.n3t.mobile.data.model.navigation

import com.n3t.mobile.data.model.routing.SampleRouteStepData

data class CacheNavigation(var currentRoute: Any? = null,
                           var allRoutes: List<Any>? = null,
                           var primaryRouteIndex: Int = 0,
                           var sampleRouteStepData: List<SampleRouteStepData>? = null
)
