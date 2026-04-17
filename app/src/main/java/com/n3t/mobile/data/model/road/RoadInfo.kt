package com.n3t.mobile.data.model.road

import com.google.gson.annotations.SerializedName

data class RoadInfo(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("lon") val lon: Double? = null,
    @SerializedName("lat") val lat: Double? = null,
    @SerializedName("changeSpeed") val changeSpeed: Boolean? = null,
    @SerializedName("minSpeed") val minSpeed: Int? = null,
    @SerializedName("maxSpeed") val maxSpeed: Int? = null,
    @SerializedName("gocHuong") val direction: String? = null,
    @SerializedName("compass") val compass: String? = null,
    @SerializedName("name") val name: String? = null,
)

