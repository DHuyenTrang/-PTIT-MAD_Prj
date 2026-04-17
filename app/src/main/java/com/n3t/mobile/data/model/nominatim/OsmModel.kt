package com.n3t.mobile.data.model.nominatim

import com.google.gson.annotations.SerializedName

data class OsmModel (
    @SerializedName("osm_id") val osmId: Int?,
    @SerializedName("datapoint") val datapoint: ArrayList<DatapointModel>?,
    @SerializedName("relation") val relation: ArrayList<String>?,
)
