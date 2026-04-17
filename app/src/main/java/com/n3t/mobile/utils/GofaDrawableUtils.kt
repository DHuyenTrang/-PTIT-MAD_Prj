package com.n3t.mobile.utils

import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import com.n3t.mobile.R
import com.n3t.mobile.data.model.map.ModifierMap

object GofaDrawableUtils {
    fun getIconLimitSpeed(maxSpeed: Int) : Int {
        if (maxSpeed == 30) return R.drawable.speed_limit_30
        if (maxSpeed == 40) return R.drawable.speed_limit_40
        if (maxSpeed == 50) return R.drawable.speed_limit_50
        if (maxSpeed == 60) return R.drawable.speed_limit_60
        if (maxSpeed == 70) return R.drawable.speed_limit_70
        if (maxSpeed == 80) return R.drawable.speed_limit_80
        if (maxSpeed == 90) return R.drawable.speed_limit_90
        if (maxSpeed == 100) return R.drawable.speed_limit_100
        if (maxSpeed == 120) return R.drawable.speed_limit_120
        return R.drawable.speed_limit_undefined
    }

    fun navigationImageDirection(modifierMap: ModifierMap) : Int {
        return when (modifierMap) {
            ModifierMap.STRAIGHT -> R.drawable.road_straight
            ModifierMap.U_TURN -> R.drawable.road_u_turn_left
            ModifierMap.TURN_LEFT -> R.drawable.road_turn_left
            ModifierMap.TURN_RIGHT -> R.drawable.road_turn_right
            ModifierMap.SLIGHT_LEFT -> R.drawable.road_slight_left
            ModifierMap.SLIGHT_RIGHT -> R.drawable.road_slight_right
            ModifierMap.SHARP_LEFT -> R.drawable.road_sharp_left
            ModifierMap.SHARP_RIGHT -> R.drawable.road_sharp_right
        }
    }

    fun getLaneImage(laneStr: String): Int {
        return when (laneStr) {
            "left" -> R.drawable.road_turn_left
            "right" -> R.drawable.road_turn_right
            "left;through" -> R.drawable.road_left_head_lane
            "through;right" -> R.drawable.road_right_head_lane
            "reverse" -> R.drawable.road_u_turn_left
            else -> R.drawable.road_straight
        }
    }
}
