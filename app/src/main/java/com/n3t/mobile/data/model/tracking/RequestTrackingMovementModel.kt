package com.n3t.mobile.data.model.tracking

import com.google.gson.annotations.SerializedName

data class RequestTrackingMovementModel(
	@SerializedName("phoneNumber") var phoneNumber: String? = null,
	@SerializedName("lat") val lat: Double,
	@SerializedName("lon") val lon: Double,
	@SerializedName("speedMax") val speedMax: Int,
	@SerializedName("speedCurrent") val speedCurrent: Int,
	@SerializedName("bearing") val bearing: Double,
	@SerializedName("timestamp") var timestamp: Long? = null,
	@SerializedName("trackingPermission") val trackingPermission: Boolean,
)

data class RequestTrackingMovementModelV2(
	@SerializedName("lat") val lat: Double,
	@SerializedName("lon") val lon: Double,
	@SerializedName("speedMax") val speedMax: Int,
	@SerializedName("speedCurrent") val speedCurrent: Int,
	@SerializedName("bearing") val bearing: Double,
	@SerializedName("timestamp") var timestamp: Long? = null,
	@SerializedName("trackingPermission") val trackingPermission: Boolean,
	@SerializedName("osm_id") val osmId: Long?,
	@SerializedName("model_id") val modelId: String?,
	@SerializedName("model_name") val modelName: String?,
	@SerializedName("altitude") val altitude: Float?,
	@SerializedName("os") val os: String?,
	@SerializedName("os_version") val osVersion: String?,
	@SerializedName("app_version") val appVersion: String?,
	@SerializedName("protocol") val protocol: Int?,
)

