package com.n3t.mobile.data.datasources.remote

import com.n3t.mobile.data.api.ApiEndPoint
import com.n3t.mobile.data.model.goong.directions.GoongDirectionsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GoongDirectionsApiService {

    @GET(ApiEndPoint.GOONG_DIRECTION)
    suspend fun getDirections(
        @Query("api_key") apiKey: String,
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("vehicle") vehicle: String = "car",
        @Query("alternatives") alternatives: Boolean = true,
    ): Response<GoongDirectionsResponse>
}
