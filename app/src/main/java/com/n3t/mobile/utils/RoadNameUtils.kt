package com.n3t.mobile.utils

object RoadNameUtils {
    private val INSTRUCTION_PREFIX_REGEX = "(?i).*\\b(vào|theo|lên|v? phía|hu?ng|t?i|t?i)\\b\\s+".toRegex()

    fun formatRoadName(rawName: String?): String {
        if (rawName.isNullOrEmpty()) return ""
        var cleanName = rawName.split("\n").firstOrNull() ?: rawName
        cleanName = cleanName.replace(INSTRUCTION_PREFIX_REGEX, "")
        return cleanName.trim().replaceFirstChar { it.uppercase() }
    }
}
