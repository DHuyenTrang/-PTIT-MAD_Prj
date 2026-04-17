package com.n3t.mobile.data.datasources.remote

import com.n3t.mobile.data.api.ApiEndPoint
import com.n3t.mobile.data.model.google.places.GoongAutocompleteResponse
import com.n3t.mobile.data.model.google.places.GoongPlaceDetailsResponse
import com.n3t.mobile.data.model.goong.directions.GoongDirectionsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoongApiService {
    @GET(ApiEndPoint.GOONG_AUTOCOMPLETE)
    suspend fun autocomplete(
        @Query("api_key") apiKey: String,
        @Query("input") input: String,
        @Query("location") location: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("radius") radius: Int? = null,
        @Query("more_compound") moreCompound: Boolean? = true,
        @Query("has_deprecated_administrative_unit") hasDeprecatedAdminUnit: Boolean? = true,
    ): Response<GoongAutocompleteResponse>

    @GET(ApiEndPoint.GOONG_DETAIL)
    suspend fun getPlaceDetails(
        @Query("api_key") apiKey: String,
        @Query("place_id") placeId: String,
        @Query("has_deprecated_administrative_unit") hasDeprecatedAdminUnit: Boolean? = true,
    ): Response<GoongPlaceDetailsResponse>

    @GET(ApiEndPoint.GOONG_DIRECTION)
    suspend fun getDirections(
        @Query("api_key") apiKey: String,
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("vehicle") vehicle: String = "car",
        @Query("alternatives") alternatives: Boolean = true,
    ): Response<GoongDirectionsResponse>
}
