package com.n3t.mobile.data.model.media

import com.google.gson.annotations.SerializedName

data class SearchMediaResponse(
    @SerializedName("target") val target: String,
    @SerializedName("query") val query: String,
    @SerializedName("url") val url: String?,
) {
}

enum class MediaTypeSearch(val value: String) {
    ZINGMP3("zingmp3"),
    YOUTUBE("youtube");

    companion object {
        fun fromString(value: String): MediaTypeSearch {
            return when (value) {
                "zingmp3" -> ZINGMP3
                "youtube" -> YOUTUBE
                else -> YOUTUBE
            }
        }
    }
}
