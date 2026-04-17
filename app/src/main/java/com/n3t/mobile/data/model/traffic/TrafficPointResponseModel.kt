package com.n3t.mobile.data.model.traffic

import com.google.gson.annotations.SerializedName

data class TrafficPointResponseModel(
	@SerializedName("category") var category: TrafficSignCategory? = null,
	@SerializedName("data") var data: List<PointLocationModel>?,
)

