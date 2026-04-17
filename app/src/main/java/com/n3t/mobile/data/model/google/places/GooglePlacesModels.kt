package com.n3t.mobile.data.model.google.places

import com.google.gson.annotations.SerializedName

data class GoongAutocompleteResponse(
    @SerializedName("predictions") val predictions: List<GoongPrediction> = emptyList(),
    @SerializedName("status") val status: String? = null
)

data class GoongPrediction(
    @SerializedName("description") val description: String? = null,
    @SerializedName("place_id") val placeId: String? = null,
    @SerializedName("structured_formatting") val structuredFormatting: GoongStructuredFormatting? = null,
    @SerializedName("distance_meters") val distanceMeters: Int? = null
)

data class GoongStructuredFormatting(
    @SerializedName("main_text") val mainText: String? = null,
    @SerializedName("secondary_text") val secondaryText: String? = null
)

data class GoongPlaceDetailsResponse(
    @SerializedName("result") val result: GoongPlaceResult? = null,
    @SerializedName("status") val status: String? = null
)

data class GoongPlaceResult(
    @SerializedName("place_id") val placeId: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("formatted_address") val formattedAddress: String? = null,
    @SerializedName("geometry") val geometry: GoongGeometry? = null
)

data class GoongGeometry(
    @SerializedName("location") val location: GoongLocation? = null
)

data class GoongLocation(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
)
