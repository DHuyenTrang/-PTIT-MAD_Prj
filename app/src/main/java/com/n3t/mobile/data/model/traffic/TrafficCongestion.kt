package com.n3t.mobile.data.model.traffic

import com.google.gson.annotations.SerializedName

data class TrafficCongestionResponse(
	@SerializedName("level1") val level1: List<String>? = null,
	@SerializedName("level2") val level2: List<String>? = null,
	@SerializedName("level3") val level3: List<String>? = null,
)

data class TrafficCongestionRequest(
	@SerializedName("list_geohash") val listGeohash: List<String>,
)

data class SampleLevel(
	@SerializedName("geometry") val geometry: ArrayList<String>? = null,
	@SerializedName("class") val classData: String? = null,
)
data class Sample(
	@SerializedName("geohash_index") val geohashIndex: String? = null,
	@SerializedName("level1") val level1: ArrayList<SampleLevel>? = null,
	@SerializedName("level2") val level2: ArrayList<SampleLevel>? = null,
	@SerializedName("level3") val level3: ArrayList<SampleLevel>? = null,
)
data class SampleData(
	@SerializedName("data") val data: ArrayList<Sample>? = null,
)
