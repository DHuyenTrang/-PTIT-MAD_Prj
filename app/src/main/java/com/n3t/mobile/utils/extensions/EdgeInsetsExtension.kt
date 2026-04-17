package com.n3t.mobile.utils.extensions

import com.mapbox.maps.EdgeInsets

fun EdgeInsets.copyWith(
    top: Double? = null,
    left: Double? = null,
    bottom: Double? = null,
    right: Double? = null
) : EdgeInsets {
    return EdgeInsets(
        top ?: this.top,
        left ?: this.left,
        bottom ?: this.bottom,
        right ?: this.right
    )
}

