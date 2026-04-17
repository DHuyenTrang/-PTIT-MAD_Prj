package com.n3t.mobile.data.model.routing

import com.google.gson.annotations.SerializedName
import com.mapbox.geojson.Point


data class SpeedInterval(
    @SerializedName("start_polyline_point_index")
    val startIndex: Int,
    @SerializedName("end_polyline_point_index")
    val endIndex: Int,
    @SerializedName("speed")
    val speed: String,
) {}

data class CongestionSegment(
    val startIndex: Int,
    val endIndex: Int,
    val congestion: Int
)
