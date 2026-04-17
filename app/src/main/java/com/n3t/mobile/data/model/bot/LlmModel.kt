package com.n3t.mobile.data.model.bot

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.annotations.SerializedName

import java.lang.reflect.Type
data class LlmModel(
    @SerializedName("Text") val text: String? = null,
    @SerializedName("is_final") val isFinal: Boolean? = null
)

fun LlmModel.convertToBotModel(): BotModel {
    if (!text.isNullOrEmpty()) {
        if (text != "") {
            return try {
                val gson = Gson()
                val dataModel = gson.fromJson(text, BotModel::class.java)

                dataModel
            } catch (e: Exception) {

                e.printStackTrace()
                BotModel("ERROR", "K?t thúc l?ng nghe ", null, null)
            }
        }

        return BotModel("ERROR", "", null, null)
    }

    return BotModel("ERROR", "Không có k?t qu?", null, null)
}

data class LlmUserModel(
    @SerializedName("User") val user: String,
    @SerializedName("Assistant") val assistant: String? // Có th? null
)

data class LlmSystemModel(
    @SerializedName("System") val system: String
)

data class LlmUserRequestModel(
    @SerializedName("User") val user: String
)

data class LlmAssistantModel(
    @SerializedName("Assistant") val assistant: String
)

data class LlmMessageModel(
//    @SerializedName("Request") val request: List<LlmRequestModel>
    @SerializedName("Request") val request: List<Map<String, String>>
)

//sealed class LlmRequestModel {
//    data class System(@SerializedName("System") val system: String) : LlmRequestModel()
//    data class User(@SerializedName("User") val user: String) : LlmRequestModel()
//    data class Assistant(@SerializedName("Assistant") val assistant: String) : LlmRequestModel()
//
//    class LlmRequestModelDeserializer : JsonDeserializer<LlmRequestModel> {
//        override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): LlmRequestModel {
//            val jsonObject = json.asJsonObject
//
//            return when {
//                jsonObject.has("System") -> {
//                    val system = context.deserialize<LlmSystemModel>(jsonObject, LlmSystemModel::class.java)
//                    System(system.system)
//                }
//                jsonObject.has("User") -> {
//                    val user = context.deserialize<LlmUserRequestModel>(jsonObject, LlmUserRequestModel::class.java)
//                    User(user.user)
//                }
//                jsonObject.has("Assistant") -> {
//                    val assistant = context.deserialize<LlmAssistantModel>(jsonObject, LlmAssistantModel::class.java)
//                    Assistant(assistant.assistant)
//                }
//                else -> throw JsonParseException("Invalid type for LlmRequestModel")
//            }
//        }
//    }
//}
