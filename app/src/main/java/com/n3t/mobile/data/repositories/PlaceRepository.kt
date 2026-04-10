package com.n3t.mobile.data.repositories

import com.n3t.mobile.core.services.NetworkService
import com.n3t.mobile.data.api.HandleApiResponse
import com.n3t.mobile.data.datasources.remote.N3TApiService
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.api_util.ErrorType
import com.n3t.mobile.data.model.api_util.Failure
import com.n3t.mobile.data.model.api_util.toApiResponseFail
import com.n3t.mobile.data.model.search_map.PlaceDetailResponseModel
import com.n3t.mobile.data.model.search_map.PlaceResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface PlaceRepository {
    suspend fun searchPlace(text: String, lat: Float?, lng: Float?): Either<Failure, PlaceResponseModel?>
    suspend fun getPlaceDetail(placeId: String): Either<Failure, PlaceDetailResponseModel?>
}

class PlaceRepositoryImpl(
    private val remoteDataSource: N3TApiService,
    private val networkService: NetworkService,
) : PlaceRepository {

    override suspend fun searchPlace(text: String, lat: Float?, lng: Float?): Either<Failure, PlaceResponseModel?> =
        withContext(Dispatchers.IO) {
            HandleApiResponse.whenInternetError(networkService)?.let { return@withContext it }
            try {
                val response = remoteDataSource.searchMapByText(text, lat, lng).execute()
                if (response.isSuccessful) {
                    return@withContext Either.success(response.body())
                }
                val responseFail = response.toApiResponseFail()
                return@withContext Either.failure(
                    Failure(ErrorType.SERVER_RESPONSE_ERROR, responseFail?.message, responseFail?.code)
                )
            } catch (e: Exception) {
                return@withContext Either.failure(Failure(ErrorType.SERVER_ERROR, e.message))
            }
        }

    override suspend fun getPlaceDetail(placeId: String): Either<Failure, PlaceDetailResponseModel?> =
        withContext(Dispatchers.IO) {
            HandleApiResponse.whenInternetError(networkService)?.let { return@withContext it }
            try {
                val response = remoteDataSource.searchDetailMap(placeId).execute()
                if (response.isSuccessful) {
                    return@withContext Either.success(response.body())
                }
                val responseFail = response.toApiResponseFail()
                return@withContext Either.failure(
                    Failure(ErrorType.SERVER_RESPONSE_ERROR, responseFail?.message, responseFail?.code)
                )
            } catch (e: Exception) {
                return@withContext Either.failure(Failure(ErrorType.SERVER_ERROR, e.message))
            }
        }
}
