package com.n3t.mobile.data.model.traffic

enum class TrafficSignInstruction {
	ENTER_URBAN,
	EXIT_TOWN,
	TOLL_BOOTH,
	START_NO_OVERTAKING,
	END_OVERTAKING,
	NONE,
	ALL;

	val instruction: String
		get() {
			return when (this) {
				ENTER_URBAN -> "Entering Urban Area"
				EXIT_TOWN -> "Town Exit Point"
				TOLL_BOOTH -> "Toll Booth"
				START_NO_OVERTAKING -> "Start of No Overtaking"
				END_OVERTAKING -> "End of No Overtaking"
				NONE -> "none"
				ALL -> "all"
			}
		}
	val id: String
		get() {
			return when (this) {
				ENTER_URBAN -> "Entering Urban Area"
				EXIT_TOWN -> "Town Exit Point"
				TOLL_BOOTH -> "Toll Booth"
				START_NO_OVERTAKING -> "Start of No Overtaking"
				END_OVERTAKING -> "End of No Overtaking"
				NONE -> "none"
				ALL -> "all"
			}
		}
}

