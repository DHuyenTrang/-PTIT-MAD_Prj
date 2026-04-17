package com.n3t.mobile.data.model.place_flow

import java.io.Serializable
import com.n3t.mobile.data.model.goong.directions.GoongRoute

data class CoordinateModel(
    val latitude: Double,
    val longitude: Double,
) : Serializable

data class PlaceSuggestionUiModel(
    val placeId: String,
    val title: String,
    val subtitle: String,
    val distanceMeters: Int? = null,
)

data class PlaceDetailUiModel(
    val placeId: String,
    val name: String,
    val formattedAddress: String,
    val latitude: Double,
    val longitude: Double,
    val distanceMeters: Int? = null,
) : Serializable {
    val coordinate: CoordinateModel
        get() = CoordinateModel(latitude, longitude)
}

data class RouteOptionUiModel(
    val id: String,
    val encodedPolyline: String,
    val distanceMeters: Int,
    val durationSeconds: Int,
    val durationText: String,
    val distanceText: String,
    val label: String,
    val isSelected: Boolean = false,
    val rawRoute: GoongRoute? = null,
) : Serializable

data class NavigationStubArgs(
    val destinationName: String,
    val destinationAddress: String,
    val origin: CoordinateModel,
    val destination: CoordinateModel,
    val selectedRoute: RouteOptionUiModel,
) : Serializable
