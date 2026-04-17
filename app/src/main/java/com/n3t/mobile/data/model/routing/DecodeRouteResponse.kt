package com.n3t.mobile.data.model.routing

import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap

data class DecodeRouteResponse(
    @SerializedName("type") val type: String,
    @SerializedName("routes") val routes: List<Any>,
) {
    companion object {
        fun fromJson(json: LinkedTreeMap<String, Any>): DecodeRouteResponse {
            val type = json["type"] as? String ?: ""
            return DecodeRouteResponse(type, emptyList())
        }
    }
}
