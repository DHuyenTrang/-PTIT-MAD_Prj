package com.n3t.mobile.data.model.search_map

import com.google.gson.annotations.SerializedName

data class Prediction(
    @SerializedName("description") val description: String,
    @SerializedName("place_id") val placeId: String,
    @SerializedName("reference") val reference: String,
    @SerializedName("structured_formatting") val structuredFormatting: StructuredFormatting,
)

