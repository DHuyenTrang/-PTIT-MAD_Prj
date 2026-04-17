package com.n3t.mobile.data.model.sound

import com.n3t.mobile.utils.Constants
import com.google.gson.annotations.SerializedName

enum class SpeedLimitSoundType {
    @SerializedName("VOICE")
    VOICE,
    @SerializedName("BEEP")
    BEEP,
    @SerializedName("NONE")
    NONE
}

data class SoundSetting(
    @SerializedName("mute") val mute: Boolean = false,
    @SerializedName("warningVolume") val warningVolume: Int = Constants.VOLUME_SOUND_WARNING,
    @SerializedName("warningSpeedLimitType") val warningSpeedLimitType: SpeedLimitSoundType = SpeedLimitSoundType.VOICE,
    @SerializedName("overSpeedLimit") val overSpeedLimit: Boolean = true, // false - sound, true - Beep
    @SerializedName("trafficCameraWarning") val trafficCameraWarning: Boolean = true,
    @SerializedName("residentialAreaWarning") val residentialAreaWarning: Boolean = true,
    @SerializedName("forbiddenRoadWarning") val forbiddenRoadWarning: Boolean = true,
    @SerializedName("reduceSystemSound") val reduceSystemSound: Boolean = true,
    @SerializedName("tollStationWarning") val tollStationWarning: Boolean = true,
    @SerializedName("turnWarning") val turnWarning: Boolean = true,
    @SerializedName("speedLimitWarningNextRoad") val speedLimitWarningNextRoad : Boolean = true,
)

