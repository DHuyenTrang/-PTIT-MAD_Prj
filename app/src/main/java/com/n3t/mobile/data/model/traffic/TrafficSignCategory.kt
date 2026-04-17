package com.n3t.mobile.data.model.traffic

import com.google.gson.annotations.SerializedName

enum class TrafficSignCategory {
	@SerializedName("traffic-sign")
	TRAFFIC_SIGN,
	@SerializedName("camera")
	CAMERA,
	@SerializedName("others")
	OTHERS,
}

