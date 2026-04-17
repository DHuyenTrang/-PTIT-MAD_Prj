package com.n3t.mobile.data.model.car

import com.google.gson.annotations.SerializedName

data class PenaltyModel(
    @SerializedName("time") val time: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("action") val action: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("addrPenalty") val addrPenalty: String?,
    @SerializedName("addrProcessPenalty") val addrProcessPenalty: String?,
)

