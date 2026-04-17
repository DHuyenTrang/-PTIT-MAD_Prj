package com.n3t.mobile.data.model.nominatim

import com.google.gson.annotations.SerializedName

data class DatapointModel(
    @SerializedName("id") val id: Int?,
    @SerializedName("compass") val compass: String?,
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lon") val lon: Double?,
    @SerializedName("traffic_sign_id") val trafficSignId: Int?,
    @SerializedName("direction") val direction: Double?,
)
