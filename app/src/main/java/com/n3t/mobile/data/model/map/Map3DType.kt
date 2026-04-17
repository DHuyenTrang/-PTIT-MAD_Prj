package com.n3t.mobile.data.model.map

enum class Map3DType {
    DUSK,
    DAWN,
    DAY,
    NIGHT,
    FLOW_THEME,
    AUTO;

    fun getMapStyle(): String? {
        return when (this) {
            DUSK -> "dusk"
            DAWN -> "dawn"
            DAY -> "day"
            NIGHT -> "night"
            else -> null
        }
    }
}

