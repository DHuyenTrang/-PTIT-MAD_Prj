package com.n3t.mobile.data.model.tracking

import com.google.gson.annotations.SerializedName

data class TrackingHistoryInformation(
	@SerializedName("lat") val lat: Double,
	@SerializedName("lon") val lon: Double,
	@SerializedName("speed_max") val speedMax: Int,
	@SerializedName("speed_current") val speedCurrent: Int?,
	@SerializedName("bearing") val bearing: Double,
	@SerializedName("timestamp") var timestamp: Long,
	@SerializedName("name") var name: String?
)

