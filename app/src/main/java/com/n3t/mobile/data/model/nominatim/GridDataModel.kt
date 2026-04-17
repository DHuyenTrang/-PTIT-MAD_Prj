package com.n3t.mobile.data.model.nominatim

import com.google.gson.annotations.SerializedName

data class GridDataModel (
    @SerializedName("geohash") val geohash: String?,
    @SerializedName("osm") val osm: ArrayList<OsmModel>?,
)
