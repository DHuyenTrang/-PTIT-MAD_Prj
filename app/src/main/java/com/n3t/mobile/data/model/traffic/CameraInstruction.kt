package com.n3t.mobile.data.model.traffic

enum class CameraInstruction {
	SPEED_CAMERA,
	MOBILE_SPEED_CAMERA,
	TRAFFIC_CAMERA,
	SPEED_TRAP_AREA,
	RED_LIGHT_CAMERA,
	RED_LIGHT_AND_SPEED_CAMERA,
	NONE,
	ALL;

	val instruction: String
		get() {
			return when (this) {
				NONE -> "none"
				ALL -> "all"
				SPEED_CAMERA -> "Speed Camera"
				MOBILE_SPEED_CAMERA -> "Mobile Speed Camera"
				SPEED_TRAP_AREA -> "Speed Trap Area"
				TRAFFIC_CAMERA -> "Traffic Camera"
				RED_LIGHT_CAMERA -> "Red Light Camera"
				RED_LIGHT_AND_SPEED_CAMERA -> "Red Light and Speed Camera"
			}
		}
}

