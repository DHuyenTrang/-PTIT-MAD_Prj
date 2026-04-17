package com.n3t.mobile.data.model.traffic

import com.google.gson.annotations.SerializedName

data class TrafficBulletin(
    @SerializedName("id") val id: Int?,
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lon") val lon: Double?,
    @SerializedName("osm_combination_id") val osmCombinationId: String?,
    @SerializedName("ai_road_id") val aiRoadId: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("traffic_type") val trafficType: TrafficType?,
    @SerializedName("name") val name: String?,
    @SerializedName("sta_cty") val staCty: String?,
    @SerializedName("content") val content: String?,
    @SerializedName("forever") val forever: Boolean?,
    @SerializedName("audio_link") val audioLink: String?,
    @SerializedName("expire_time") val expireTime: Long?,
    @SerializedName("image") val images: List<String>?,
    @SerializedName("bearing") val bearing: Double?,
)

