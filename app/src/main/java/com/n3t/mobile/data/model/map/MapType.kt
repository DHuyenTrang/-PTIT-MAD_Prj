package com.n3t.mobile.data.model.map

enum class MapType {
    MAP_4D, MAP_3D, SATELLITE, AUTO_FOCUS
}

fun MapType.getDescription(): String {
    return when (this) {
        MapType.MAP_4D -> "MAP_4D"
        MapType.MAP_3D -> "MAP_3D"
        MapType.SATELLITE -> "SATELLITE"
        MapType.AUTO_FOCUS -> "AUTO_FOCUS"
    }
}
