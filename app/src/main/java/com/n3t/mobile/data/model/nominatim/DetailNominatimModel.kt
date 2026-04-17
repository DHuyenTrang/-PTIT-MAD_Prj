package com.n3t.mobile.data.model.nominatim

import com.google.gson.annotations.SerializedName

data class DetailNominatimModel (
    @SerializedName("place_id") val placeId: Int?,
    @SerializedName("osm_id") val osmId: Int?,
    @SerializedName("parent_place_id") val parentPlaceId: Int?,
    @SerializedName("category") val category: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("extratags") val extratags: ExtratagModel?,
    @SerializedName("geometry") val geometry: NominatimGeometryModel?,
    @SerializedName("lat") val lat: String?,
    @SerializedName("lon") val lon: String?,
)
