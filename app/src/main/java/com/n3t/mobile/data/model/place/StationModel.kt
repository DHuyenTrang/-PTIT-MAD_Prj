package com.n3t.mobile.data.model.place

import com.n3t.mobile.data.model.bot.OpenAIMessage
import com.google.gson.annotations.SerializedName
data class StationModel (
    @SerializedName("id") val id : Int?,
    @SerializedName("display_name") val displayName : String?,
    @SerializedName("position") val position: List<Double>?,
    @SerializedName("geohash6") val geohash6: String?,
    @SerializedName("poi_type") val poiType: String?,
    @SerializedName("address") val address : String?,
    @SerializedName("start_working_time") val startWorkingTime: String?,
    @SerializedName("end_working_time") val endWorkingTime: String?,
    @SerializedName("off_sunday") val offSunday: Boolean?,
    @SerializedName("extend_data") val extendData: ExtendData?

)
data class ExtendData(
    @SerializedName("others")
    val others: List<List<String>>,
    @SerializedName("capacity_type")
    val capacityType: Int?
)

enum class StationProvider(val rawValue: String) {
    CHARGE("charge_station"),
    FUEL("fuel_station"),
    NONE("");

    // Hàm tr? v? raw string
    fun getProvider(): String {
        return rawValue
    }
}
