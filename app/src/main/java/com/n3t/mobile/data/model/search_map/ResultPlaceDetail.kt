package com.n3t.mobile.data.model.search_map

import com.google.gson.annotations.SerializedName

data class ResultPlaceDetail(
    @SerializedName("place_id") val placeId: String,
    @SerializedName("formatted_address") val formattedAddress: String,
    @SerializedName("geometry") val geometry: GoongGeometry,
    @SerializedName("name") val name: String,
    @SerializedName("customName") val customName: String?
)

