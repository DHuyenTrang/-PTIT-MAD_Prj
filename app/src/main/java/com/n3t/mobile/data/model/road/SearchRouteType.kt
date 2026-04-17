package com.n3t.mobile.data.model.road

enum class SearchRouteType(val value: Int)  {
    OSRM(0), VALHALLA(1), GOOGLE(2);

    override fun toString(): String {
        return when (this) {
            OSRM -> "osrm"
            VALHALLA -> "valhalla"
            GOOGLE -> "google"
        }
    }
}

