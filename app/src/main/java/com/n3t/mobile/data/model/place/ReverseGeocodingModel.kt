package com.n3t.mobile.data.model.place

import com.google.gson.annotations.SerializedName

data class LocationInformation (
    @SerializedName("display_name") val displayName : String,
    @SerializedName("address") val address: AddressModel
)

