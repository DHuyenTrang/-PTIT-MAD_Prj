package com.n3t.mobile.data.model.traffic

import com.google.gson.annotations.SerializedName
import com.mapbox.geojson.Point

data class CongestionSegment (
    var listPoints: List<Point>,
    var congestionLevel: CongestionLevel
)
