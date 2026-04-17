package com.n3t.mobile.data.model.app_profile

import com.google.gson.annotations.SerializedName

data class AppProfile(
    @SerializedName("flag") val flag: Boolean,
    @SerializedName("image") val image: String,
    @SerializedName("video") val video: String,
    @SerializedName("icon") val icon: String,
    @SerializedName("secure") val secure: Boolean?,
    @SerializedName("is_update_password") val isUpdatePassword: Boolean?,

    @SerializedName("sign_in_provider") val signInProvider: String?,
    @SerializedName("uuid") val uuid: String?,
    @SerializedName("display_name") val displayName: String?,
    @SerializedName("payment") val payment: Boolean?,
    @SerializedName("license_status") val licenseStatus: String?,
)



data class TestModel(
    @SerializedName("id") val id: Int?,
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lon") val lon: Double?,
    @SerializedName("bearing") val bearing: Double?,
    @SerializedName("correctOsmId") val correctOsmId: Int?,
)
