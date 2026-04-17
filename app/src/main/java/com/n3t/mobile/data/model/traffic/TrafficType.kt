package com.n3t.mobile.data.model.traffic

import com.google.gson.annotations.SerializedName

enum class TrafficType {
	@SerializedName("Traffic Jam")
	TRAFFIC_JAM,

	@SerializedName("Flood")
	FLOOD,

	@SerializedName("Accident")
	ACCIDENT,

	@SerializedName("Restricted Road")
	RESTRICTED_ROAD,

	@SerializedName("Others")
	OTHERS
}

