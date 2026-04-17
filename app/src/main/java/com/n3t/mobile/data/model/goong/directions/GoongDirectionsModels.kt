package com.n3t.mobile.data.model.goong.directions

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GoongDirectionsResponse(
    @SerializedName("geocoded_waypoints") val geocodedWaypoints: List<GoongGeocodedWaypoint>?,
    @SerializedName("routes") val routes: List<GoongRoute>?,
    @SerializedName("status") val status: String?
) : Serializable

data class GoongGeocodedWaypoint(
    @SerializedName("geocoder_status") val geocoderStatus: String?,
    @SerializedName("place_id") val placeId: String?
) : Serializable

data class GoongRoute(
    @SerializedName("bounds") val bounds: GoongBounds?,
    @SerializedName("copyrights") val copyrights: String?,
    @SerializedName("legs") val legs: List<GoongLeg>?,
    @SerializedName("overview_polyline") val overviewPolyline: GoongPolyline?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("warnings") val warnings: List<String>?,
    @SerializedName("waypoint_order") val waypointOrder: List<Int>?
) : Serializable

data class GoongBounds(
    @SerializedName("northeast") val northeast: GoongLocation?,
    @SerializedName("southwest") val southwest: GoongLocation?
) : Serializable

data class GoongLeg(
    @SerializedName("distance") val distance: GoongDistance?,
    @SerializedName("duration") val duration: GoongDuration?,
    @SerializedName("end_address") val endAddress: String?,
    @SerializedName("end_location") val endLocation: GoongLocation?,
    @SerializedName("start_address") val startAddress: String?,
    @SerializedName("start_location") val startLocation: GoongLocation?,
    @SerializedName("steps") val steps: List<GoongStep>?
) : Serializable

data class GoongStep(
    @SerializedName("distance") val distance: GoongDistance?,
    @SerializedName("duration") val duration: GoongDuration?,
    @SerializedName("end_location") val endLocation: GoongLocation?,
    @SerializedName("html_instructions") val htmlInstructions: String?,
    @SerializedName("polyline") val polyline: GoongPolyline?,
    @SerializedName("start_location") val startLocation: GoongLocation?,
    @SerializedName("travel_mode") val travelMode: String?,
    @SerializedName("maneuver") val maneuver: String?
) : Serializable

data class GoongDistance(
    @SerializedName("text") val text: String?,
    @SerializedName("value") val value: Int?
) : Serializable

data class GoongDuration(
    @SerializedName("text") val text: String?,
    @SerializedName("value") val value: Int?
) : Serializable

data class GoongPolyline(
    @SerializedName("points") val points: String?
) : Serializable

data class GoongLocation(
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lng") val lng: Double?
) : Serializable
