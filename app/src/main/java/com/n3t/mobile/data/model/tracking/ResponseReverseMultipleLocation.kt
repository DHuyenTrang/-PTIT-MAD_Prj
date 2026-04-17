package com.n3t.mobile.data.model.tracking

import com.google.gson.annotations.SerializedName

data class ResponseReverseMultipleLocation(
	@SerializedName("data") var data: List<TrackingHistoryLocationName>,
)

