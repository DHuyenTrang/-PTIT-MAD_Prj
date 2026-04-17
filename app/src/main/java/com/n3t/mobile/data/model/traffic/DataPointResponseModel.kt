package com.n3t.mobile.data.model.traffic

import com.google.gson.annotations.SerializedName
import com.n3t.mobile.data.model.speed.SpeedResponseModel

data class DataPointResponseModel(
	@SerializedName("currentRoadName") val currentRoadName: String?,
	@SerializedName("speedPoints") val speedPoints: SpeedResponseModel,
	@SerializedName("trafficPoints") val trafficPoints: List<TrafficPointResponseModel>?,
)

