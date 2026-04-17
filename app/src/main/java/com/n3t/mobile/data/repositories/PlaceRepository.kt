package com.n3t.mobile.data.repositories

import com.n3t.mobile.BuildConfig
import com.n3t.mobile.core.services.NetworkService
import com.n3t.mobile.data.datasources.remote.GoongApiService
import com.n3t.mobile.data.datasources.remote.N3TApiService
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.api_util.ErrorType
import com.n3t.mobile.data.model.api_util.Failure
import com.n3t.mobile.data.model.place_flow.CoordinateModel
import com.n3t.mobile.data.model.place_flow.PlaceDetailUiModel
import com.n3t.mobile.data.model.place_flow.PlaceSuggestionUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface PlaceRepository {
    suspend fun searchSuggestions(
        query: String,
        location: CoordinateModel?,
        sessionToken: String,
    ): Either<Failure, List<PlaceSuggestionUiModel>>

    suspend fun getPlaceDetail(
        placeId: String,
        sessionToken: String?,
    ): Either<Failure, PlaceDetailUiModel>
}

class PlaceRepositoryImpl(
    private val remoteDataSource: N3TApiService,
    private val goongApiService: GoongApiService,
    private val networkService: NetworkService,
) : PlaceRepository {

    private fun goongApiKeyMissing(): Boolean {
        return BuildConfig.GOONG_API_KEY.isBlank()
    }

    private fun buildHttpErrorMessage(
        action: String,
        code: Int,
        fallback: String,
        body: String?,
    ): String {
        val compactBody = body?.replace("\n", " ")?.trim().orEmpty()
        if (compactBody.isNotBlank()) {
            return "$action thất bại ($code): $compactBody"
        }
        return "$action thất bại ($code): $fallback"
    }

    override suspend fun searchSuggestions(
        query: String,
        location: CoordinateModel?,
        sessionToken: String,
    ): Either<Failure, List<PlaceSuggestionUiModel>> = withContext(Dispatchers.IO) {
        if (!networkService.isConnected()) {
            return@withContext Either.failure(Failure(ErrorType.LOST_INTERNET))
        }
        if (goongApiKeyMissing()) {
            return@withContext Either.failure(
                Failure(
                    errorType = ErrorType.SERVER_RESPONSE_ERROR,
                    message = "Thiếu GOONG_API_KEY trong local.properties",
                )
            )
        }

        try {
            val locationStr = location?.let { "${it.latitude},${it.longitude}" }
            val radius = if (locationStr != null) 50000 else null

            val response = goongApiService.autocomplete(
                apiKey = BuildConfig.GOONG_API_KEY,
                input = query,
                location = locationStr,
                radius = radius
            )

            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                return@withContext Either.failure(
                    Failure(
                        errorType = ErrorType.SERVER_RESPONSE_ERROR,
                        message = buildHttpErrorMessage(
                            action = "Autocomplete",
                            code = response.code(),
                            fallback = response.message(),
                            body = errorBody,
                        ),
                        errorCode = response.code(),
                    )
                )
            }

            val suggestions = response.body()
                ?.predictions
                .orEmpty()
                .mapNotNull { prediction ->
                    val placeId = prediction.placeId ?: return@mapNotNull null
                    val title = prediction.structuredFormatting?.mainText
                        ?: return@mapNotNull null
                    PlaceSuggestionUiModel(
                        placeId = placeId,
                        title = title,
                        subtitle = prediction.structuredFormatting.secondaryText.orEmpty(),
                        distanceMeters = prediction.distanceMeters,
                    )
                }

            Either.success(suggestions)
        } catch (exception: Exception) {
            Either.failure(
                Failure(
                    errorType = ErrorType.SERVER_ERROR,
                    message = exception.message,
                )
            )
        }
    }

    override suspend fun getPlaceDetail(
        placeId: String,
        sessionToken: String?,
    ): Either<Failure, PlaceDetailUiModel> = withContext(Dispatchers.IO) {
        if (!networkService.isConnected()) {
            return@withContext Either.failure(Failure(ErrorType.LOST_INTERNET))
        }
        if (goongApiKeyMissing()) {
            return@withContext Either.failure(
                Failure(
                    errorType = ErrorType.SERVER_RESPONSE_ERROR,
                    message = "Thiếu GOONG_API_KEY trong local.properties",
                )
            )
        }

        try {
            val response = goongApiService.getPlaceDetails(
                apiKey = BuildConfig.GOONG_API_KEY,
                placeId = placeId
            )

            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                return@withContext Either.failure(
                    Failure(
                        errorType = ErrorType.SERVER_RESPONSE_ERROR,
                        message = buildHttpErrorMessage(
                            action = "Place detail",
                            code = response.code(),
                            fallback = response.message(),
                            body = errorBody,
                        ),
                        errorCode = response.code(),
                    )
                )
            }

            val body = response.body()?.result
            val location = body?.geometry?.location
            if (body == null || location == null) {
                return@withContext Either.failure(
                    Failure(
                        errorType = ErrorType.SERVER_RESPONSE_ERROR,
                        message = "Invalid place detail response",
                    )
                )
            }

            Either.success(
                PlaceDetailUiModel(
                    placeId = body.placeId ?: placeId,
                    name = body.name ?: body.formattedAddress.orEmpty(),
                    formattedAddress = body.formattedAddress.orEmpty(),
                    latitude = location.lat,
                    longitude = location.lng,
                )
            )
        } catch (exception: Exception) {
            Either.failure(
                Failure(
                    errorType = ErrorType.SERVER_ERROR,
                    message = exception.message,
                )
            )
        }
    }


}
