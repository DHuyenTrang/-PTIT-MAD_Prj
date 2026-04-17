package com.n3t.mobile.utils.resource

import android.graphics.Color
import com.n3t.mobile.data.model.traffic.CongestionLevel

object AppColor {
    // Color Route
    val defaultRouteColor = Color.parseColor("#4FADF8")
    val alternativeRouteColor = Color.parseColor("#8E9192")

    // Color Congestion
    private val trafficModerateColor = Color.parseColor("#FFF3A63F")
    private val trafficHeavyColor = Color.parseColor("#FFE83440")
    private val trafficSevereColor = Color.parseColor("#FF8A0E37")

    private val trafficModerateAlternativeColor = Color.parseColor("#80F3A63F")
    private val trafficHeavyAlternativeColor = Color.parseColor("#80E83440")
    private val trafficSevereAlternativeColor = Color.parseColor("#808A0E37")

    val borderRoute = Color.parseColor("#3265CB")
    val borderAlternativeRoute = Color.parseColor("#5C5F5F")
    val borderCongestionRoute = Color.parseColor("#3265CB")

    fun getColorByCongestionLevel(isAlternative: Boolean, congestionLevel: CongestionLevel): Int {
        return when(congestionLevel) {
            CongestionLevel.Unknown -> if (isAlternative) { alternativeRouteColor } else { defaultRouteColor }
            CongestionLevel.Heavy -> if (isAlternative) { trafficHeavyAlternativeColor } else { trafficHeavyColor }
            CongestionLevel.Severe -> if (isAlternative) { trafficSevereAlternativeColor } else { trafficSevereColor }
            CongestionLevel.Moderate -> if (isAlternative) { trafficModerateAlternativeColor } else { trafficModerateColor }
        }
    }
}

