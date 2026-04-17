package com.n3t.mobile.data.model.traffic

import com.n3t.mobile.data.model.map.Map3DType
import com.google.gson.annotations.SerializedName

enum class CongestionLevel(val level: Int) {
    Moderate(1),
    Heavy(2),
    Severe(3),
    Unknown(0);
    companion object {
        @JvmStatic
        fun fromString(givenLevel: Int?): CongestionLevel {
            return when (givenLevel) {
                Moderate.level -> Moderate
                Heavy.level -> Heavy
                Severe.level -> Severe
                Unknown.level -> Unknown
                else -> throw Exception("Invalid id `$givenLevel`, available ids are ${
                    CongestionLevel.values().map { it.level }}") // or a null or something
            }
        }
    }
}

