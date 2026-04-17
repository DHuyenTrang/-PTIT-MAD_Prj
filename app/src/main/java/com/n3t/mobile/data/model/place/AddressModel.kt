package com.n3t.mobile.data.model.place

import com.google.gson.annotations.SerializedName

data class AddressModel (
    @SerializedName("road") val road : String?,
    @SerializedName("city") val city: String?,
    @SerializedName("county") val county: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("country") val country : String?,
    @SerializedName("neighbourhood") val neighbourhood: String?,
    @SerializedName("city_district") val cityDistrict: String?
)
