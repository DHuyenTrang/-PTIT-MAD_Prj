package com.n3t.mobile.data.model.speed

import com.n3t.mobile.data.model.road.RoadInfo
import com.google.gson.annotations.SerializedName

data class SpeedResponseModel(
    @SerializedName("data") val data: List<RoadInfo>,
    @SerializedName("currentRoad") val currentRoad: RoadInfo?,
)

