package com.n3t.mobile.data.model.search_map

import com.google.gson.annotations.SerializedName

data class GoongLocation(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
)

