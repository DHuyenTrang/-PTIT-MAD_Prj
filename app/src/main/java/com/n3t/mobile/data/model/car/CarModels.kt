package com.n3t.mobile.data.model.car

import com.google.gson.annotations.SerializedName

data class LookupLicensePlateRequest(
    @SerializedName("license_plates") val licensePlates: List<String>,
)

data class TrafficInfoModel(
    @SerializedName("license_plate") val licensePlate: String? = null,
    @SerializedName("violation_type") val violationType: String? = null,
    @SerializedName("violation_time") val violationTime: String? = null,
    @SerializedName("violation_location") val violationLocation: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("resolution_agency") val resolutionAgency: String? = null,
)

data class AddLicensePlateRequest(
    @SerializedName("license_plate") val licensePlate: String,
)

data class RequestDeleteLicensePlate(
    @SerializedName("license_plates") val licensePlates: List<String>,
)
