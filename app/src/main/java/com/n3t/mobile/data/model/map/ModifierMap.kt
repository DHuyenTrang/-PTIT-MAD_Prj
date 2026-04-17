package com.n3t.mobile.data.model.map

import com.n3t.mobile.R

enum class ModifierMap {
	U_TURN, SHARP_RIGHT, SHARP_LEFT, TURN_LEFT, TURN_RIGHT, SLIGHT_RIGHT, SLIGHT_LEFT, STRAIGHT;

	companion object {
		fun getValueOf(rawValue: String): ModifierMap {
			return when (rawValue) {
				"uturn" -> U_TURN
				"sharp right" -> SHARP_RIGHT
				"sharp left" -> SHARP_LEFT
				"left" -> TURN_LEFT
				"right" -> TURN_RIGHT
				"slight right" -> SLIGHT_RIGHT
				"slight left" -> SLIGHT_LEFT
				else -> STRAIGHT
			}
		}
	}

	/** Returns the Vietnamese instruction label for this modifier. */
	fun toInstructionLabel(): String = when (this) {
		STRAIGHT -> "đi thẳng"
		U_TURN -> "quay đầu"
		TURN_LEFT -> "rẽ trái"
		TURN_RIGHT -> "rẽ phải"
		SLIGHT_LEFT -> "chếch hướng trái"
		SLIGHT_RIGHT -> "chếch hướng phải"
		SHARP_LEFT -> "quẹo trái"
		SHARP_RIGHT -> "quẹo phải"
	}

	/** Returns the drawable resource for this maneuver direction. */
	fun toDrawableRes(): Int = when (this) {
		STRAIGHT -> R.drawable.road_straight
		U_TURN -> R.drawable.road_u_turn_left
		TURN_LEFT -> R.drawable.road_turn_left
		TURN_RIGHT -> R.drawable.road_turn_right
		SLIGHT_LEFT -> R.drawable.road_slight_left
		SLIGHT_RIGHT -> R.drawable.road_slight_right
		SHARP_LEFT -> R.drawable.road_sharp_left
		SHARP_RIGHT -> R.drawable.road_sharp_right
	}
}
