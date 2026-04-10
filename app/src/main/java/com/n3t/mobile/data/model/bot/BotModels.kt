package com.n3t.mobile.data.model.bot

import com.google.gson.annotations.SerializedName

data class BotModel(
    @SerializedName("skill") val skill: String? = null,
    @SerializedName("response") val response: String? = null,
    @SerializedName("action") val action: String? = null,
    @SerializedName("data") val data: String? = null,
)

data class OpenAIRequest(
    @SerializedName("model") val model: String = "gpt-3.5-turbo",
    @SerializedName("messages") val messages: List<OpenAIMessage>,
)

data class OpenAIMessage(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String,
)

data class OpenAIResponse(
    @SerializedName("id") val id: String? = null,
    @SerializedName("choices") val choices: List<OpenAIChoice>? = null,
)

data class OpenAIChoice(
    @SerializedName("message") val message: OpenAIMessage? = null,
    @SerializedName("finish_reason") val finishReason: String? = null,
)
