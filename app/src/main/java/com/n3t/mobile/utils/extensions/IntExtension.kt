package com.n3t.mobile.utils.extensions

import android.content.res.Resources

fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
}

fun Int.toKmCount(): String {
    return if (this < 1000) {
        "$this m"
    } else {
        "${this / 1000} km"
    }
}

fun Int.roundToNearest(value: Int): Int {
    return (Math.round(this / value.toDouble()) * value).toInt()
}

private fun Int.toHexString(): String {
    return String.format("#%06X", 0xFFFFFF and this)
}
