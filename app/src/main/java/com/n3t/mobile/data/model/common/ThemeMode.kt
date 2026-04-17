package com.n3t.mobile.data.model.common

import androidx.appcompat.app.AppCompatDelegate
import com.n3t.mobile.R

enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

fun ThemeMode.getTheme() : Int {
    return when (this) {
        ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
        ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
}

fun ThemeMode.getImage(): Int {
    return when (this) {
        ThemeMode.SYSTEM -> R.drawable.ic_theme_auto
        ThemeMode.LIGHT -> R.drawable.ic_theme_light
        ThemeMode.DARK -> R.drawable.ic_theme_dark
    }
}

fun ThemeMode.getTitleResource(): Int {
    return when (this) {
        ThemeMode.SYSTEM -> R.string.auto
        ThemeMode.LIGHT -> R.string.light
        ThemeMode.DARK -> R.string.dark
    }
}

fun ThemeMode.getDescriptionResource(): Int {
    return when (this) {
        ThemeMode.SYSTEM -> R.string.theme_auto_description
        ThemeMode.LIGHT -> R.string.theme_light_description
        ThemeMode.DARK -> R.string.theme_dark_description
    }
}

