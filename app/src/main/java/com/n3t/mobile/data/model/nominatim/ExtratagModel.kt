package com.n3t.mobile.data.model.nominatim

import com.google.gson.annotations.SerializedName

data class ExtratagModel (
    @SerializedName("oneway") val oneway: String?,
    @SerializedName("maxspeed") val maxspeed: String?,
    @SerializedName("turn:lanes") val turnLanes: String?,
)
