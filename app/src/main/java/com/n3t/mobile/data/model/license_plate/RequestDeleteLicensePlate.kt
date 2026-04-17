package com.n3t.mobile.data.model.license_plate

import com.google.gson.annotations.SerializedName

data class RequestDeleteLicensePlate (
    @SerializedName("license_plate") val licensePlate : List<String>
)

