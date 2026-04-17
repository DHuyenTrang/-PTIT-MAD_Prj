package com.n3t.mobile.data.model.search_map

import com.google.gson.annotations.SerializedName

data class GoongGeometry(
    @SerializedName("location") val location: GoongLocation,
)

