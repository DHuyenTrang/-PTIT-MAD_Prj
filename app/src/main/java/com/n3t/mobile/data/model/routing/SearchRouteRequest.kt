package com.n3t.mobile.data.model.routing

import com.google.gson.annotations.SerializedName

data class SearchRouteRequest(
    @SerializedName("avoid_motorway") val avoidMotorway: Boolean,
    @SerializedName("avoid_tolls") val avoidTolls: Boolean,
    @SerializedName("bearing") val bearing: Float,
    @SerializedName("locations") var locations: List<Map<String, Double>>,
    @SerializedName("prefer_short_step") val preferShortStep: Boolean? = null,
    @SerializedName("provider") val provider: Int = 1, // 1: valhalla, 2: google
    @SerializedName("state") val state: String = "depart", // "offroute" or "depart"
)
