package com.n3t.mobile.data.repositories

import com.n3t.mobile.core.services.NetworkService
import com.n3t.mobile.data.datasources.remote.StationApiService
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.api_util.ErrorType
import com.n3t.mobile.data.model.api_util.Failure
import com.n3t.mobile.data.model.place.StationModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface StationRepository {
    suspend fun getStations(
        lat: Double,
        lon: Double,
        poiType: String,
    ): Either<Failure, List<StationModel>>
}

class StationRepositoryImpl(
    private val remote: StationApiService,
    private val networkService: NetworkService,
) : StationRepository {

    override suspend fun getStations(
        lat: Double,
        lon: Double,
        poiType: String,
    ): Either<Failure, List<StationModel>> = withContext(Dispatchers.IO) {
        if (!networkService.isConnected()) {
            return@withContext Either.failure(Failure(ErrorType.LOST_INTERNET))
        }
        try {
            val response = remote.getPOIS(lat, lon, poiType).execute()
            if (response.isSuccessful) {
                return@withContext Either.success(response.body().orEmpty())
            }
            return@withContext Either.failure(
                Failure(
                    errorType = ErrorType.SERVER_RESPONSE_ERROR,
                    message = "Lấy danh sách trạm thất bại (${response.code()})",
                    errorCode = response.code(),
                )
            )
        } catch (e: Exception) {
            return@withContext Either.failure(
                Failure(
                    errorType = ErrorType.SERVER_ERROR,
                    message = e.message,
                )
            )
        }
    }
}
