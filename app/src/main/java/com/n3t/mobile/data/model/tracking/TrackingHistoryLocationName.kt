package com.n3t.mobile.data.model.tracking

import com.google.gson.annotations.SerializedName

data class TrackingHistoryLocationName(
	@SerializedName("lat") val lat: Double,
	@SerializedName("lon") val lon: Double,
	@SerializedName("name") var name: String?,
)

