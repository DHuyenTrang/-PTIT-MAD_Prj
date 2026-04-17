package com.n3t.mobile.data.model.routing

import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap

data class RouteResponse(
    @SerializedName("type") val type: String,
    @SerializedName("routes") val routes: List<Any>,
) {
    companion object {
        fun fromJson(json: LinkedTreeMap<String, Any>): RouteResponse {
            val type = json["type"] as? String ?: ""
            return RouteResponse(type, emptyList())
        }
    }
}

data class SampleRouteStepData(
    @SerializedName("step_geometry") val stepGeometry: String? = null,
    @SerializedName("geohash") val geohash: ArrayList<String>? = null,
)
