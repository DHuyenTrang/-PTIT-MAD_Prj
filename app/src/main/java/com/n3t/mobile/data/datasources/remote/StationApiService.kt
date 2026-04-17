package com.n3t.mobile.data.datasources.remote

import com.n3t.mobile.data.api.ApiEndPoint
import com.n3t.mobile.data.model.place.StationModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface StationApiService {
    @GET(ApiEndPoint.GET_POIS_STATION)
    fun getPOIS(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("poi_type") poiType: String,
    ): Call<ArrayList<StationModel>>
}
