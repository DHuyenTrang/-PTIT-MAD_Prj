package com.n3t.mobile.data.model.sound

data class SoundQueue(
    val id: Int? = null,
    val soundType: SoundType,
    val sound: Int,
    val timestamp: Long,
)

