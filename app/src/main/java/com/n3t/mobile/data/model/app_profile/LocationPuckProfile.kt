package com.n3t.mobile.data.model.app_profile

enum class LocationPuckType {
    NATIONAL_FLAG,
    CAR_ICON_3D,
    NORMAL
}

data class LocationPuckProfile(
    val type: LocationPuckType = LocationPuckType.NORMAL,
    val icon: String = ""
)

