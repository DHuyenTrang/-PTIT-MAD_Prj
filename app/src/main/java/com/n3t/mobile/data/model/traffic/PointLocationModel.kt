package com.n3t.mobile.data.model.traffic

import com.google.gson.annotations.SerializedName

class PointLocationModel(
	@SerializedName("id") val id: Int?,
	@SerializedName("lat") val lat: Double?,
	@SerializedName("lon") val lon: Double?,
	@SerializedName("compass") val compass: String?,
	@SerializedName("instruction") val instruction: String?,
)

