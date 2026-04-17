package com.n3t.mobile.data.model.tracking

data class TrackingHistoryTripInformation(
	val waypoint: TrackingHistoryInformation,
	val destination: TrackingHistoryInformation,
	val waypointName: TrackingHistoryLocationName,
	val destinationName: TrackingHistoryLocationName,
)

