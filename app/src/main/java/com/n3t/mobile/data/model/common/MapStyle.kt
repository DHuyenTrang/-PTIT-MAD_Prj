package com.n3t.mobile.data.model.common

import com.n3t.mobile.R

enum class MapStyleURL {
    DEFAULT, SATELLITE;

    companion object {
        private val urlToEnumMap = values().associateBy { it.getStyleUrl() }

        fun fromStyleUrl(url: String): MapStyleURL? {
            return urlToEnumMap[url]
        }

        fun fromString(value: String): MapStyleURL {
            return values().find { it.name.equals(value, ignoreCase = true) } ?: DEFAULT
        }

        fun fromItemStr(value: String) : MapStyleURL {
            when (value) {
                "3D" -> return DEFAULT
                "Vệ Tinh" -> return SATELLITE
                else -> return DEFAULT
            }
        }
    }
}

fun MapStyleURL.getStyleUrl() : String {
    return when (this) {
        MapStyleURL.DEFAULT -> "mapbox://styles/nghinv09/clwq1i4y7013u01qsahs1biu6"
        MapStyleURL.SATELLITE -> "mapbox://styles/nghinv09/clwq226uk010701pn1874h3lf"
    }
}

fun MapStyleURL.getDescription() : String {
    return when (this) {
        MapStyleURL.DEFAULT -> "default"
        MapStyleURL.SATELLITE -> "satellite"
    }
}

fun MapStyleURL.getTitleResource(): Int {
    return when (this) {
        MapStyleURL.DEFAULT -> R.string.map3d
        MapStyleURL.SATELLITE -> R.string.map_satellite
    }
}



