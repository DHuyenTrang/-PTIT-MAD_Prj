package com.n3t.mobile.data.model.tracking

import com.google.gson.annotations.SerializedName

data class RequestReverseMultipleLocation(
	@SerializedName("points") var points: List<Map<String, Double>>,
)

