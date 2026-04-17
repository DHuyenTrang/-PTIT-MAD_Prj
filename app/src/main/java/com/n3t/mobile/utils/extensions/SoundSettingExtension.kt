package com.n3t.mobile.utils.extensions

import com.n3t.mobile.R
import com.n3t.mobile.data.model.sound.SpeedLimitSoundType

fun SpeedLimitSoundType.getTitle(): Int {
    return when (this) {
        SpeedLimitSoundType.VOICE -> R.string.voice
        SpeedLimitSoundType.BEEP -> R.string.beep
        SpeedLimitSoundType.NONE -> R.string.off
    }
}

