package com.n3t.mobile.data.model.license_plate

data class MovingHistory(
    val time: String,
    val date: String,
    val startPlace: String,
    val endPlace: String,
    val distance: String,
    val timeCount: String,
    val overSpeed: String,
)

