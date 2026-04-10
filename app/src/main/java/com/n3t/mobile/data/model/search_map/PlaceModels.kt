package com.n3t.mobile.data.model.search_map

import com.google.gson.annotations.SerializedName

data class PlaceResponseModel(
    @SerializedName("predictions") val predictions: List<PlacePrediction>? = null,
)

data class PlacePrediction(
    @SerializedName("place_id") val placeId: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("structured_formatting") val structuredFormatting: StructuredFormatting? = null,
)

data class StructuredFormatting(
    @SerializedName("main_text") val mainText: String? = null,
    @SerializedName("secondary_text") val secondaryText: String? = null,
)

data class PlaceDetailResponseModel(
    @SerializedName("place_id") val placeId: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("formatted_address") val formattedAddress: String? = null,
    @SerializedName("lat") val lat: Double? = null,
    @SerializedName("lng") val lng: Double? = null,
)

data class SearchRouteRequest(
    @SerializedName("origin") val origin: LatLng,
    @SerializedName("destination") val destination: LatLng,
    @SerializedName("vehicle") val vehicle: String = "car",
)

data class LatLng(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double,
)
