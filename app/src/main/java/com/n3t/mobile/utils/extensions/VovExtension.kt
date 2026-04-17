package com.n3t.mobile.utils.extensions

import com.n3t.mobile.data.model.vov.VovChannel
import com.n3t.mobile.utils.Constants

fun VovChannel.channelName() : String {
    return when (this) {
        VovChannel.HN -> "Hà N?i"
        VovChannel.HCM -> "H? Chí Minh"
        VovChannel.MEKONG -> "Mekong"
        VovChannel.DUYEN_HAI -> "Duyên H?i"
    }
}

fun VovChannel.link() : String {
    val index = VovChannel.values().indexOf(this)
    return Constants.vovLinks[index]
}

