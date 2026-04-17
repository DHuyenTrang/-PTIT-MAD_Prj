package com.n3t.mobile.data.model.nominatim

import com.google.gson.annotations.SerializedName

data class NominatimGeometryModel (
    @SerializedName("coordinates") val coordinates: ArrayList<ArrayList<Double?>?>?,
)
