package com.n3t.mobile.data.model.car

import com.google.gson.annotations.SerializedName

data class PenaltyNotificationRequest(
    @SerializedName("enable_penalty_notification") val enablePenaltyNotification: Boolean,
)

