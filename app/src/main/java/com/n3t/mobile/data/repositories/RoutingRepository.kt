package com.n3t.mobile.data.repositories

import com.n3t.mobile.BuildConfig
import com.n3t.mobile.core.services.NetworkService
import com.n3t.mobile.data.datasources.remote.GoongApiService
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.api_util.ErrorType
import com.n3t.mobile.data.model.api_util.Failure
import com.n3t.mobile.data.model.place_flow.CoordinateModel
import com.n3t.mobile.data.model.place_flow.RouteOptionUiModel
import com.n3t.mobile.utils.RouteFormatUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface RoutingRepository {
    suspend fun computeRoutes(
        origin: CoordinateModel,
        destination: CoordinateModel,
        avoidTolls: Boolean,
        avoidHighways: Boolean,
    ): Either<Failure, List<RouteOptionUiModel>>
}

class RoutingRepositoryImpl(
    private val goongApiService: GoongApiService,
    private val networkService: NetworkService,
) : RoutingRepository {

    override suspend fun computeRoutes(
        origin: CoordinateModel,
        destination: CoordinateModel,
        avoidTolls: Boolean,
        avoidHighways: Boolean,
    ): Either<Failure, List<RouteOptionUiModel>> = withContext(Dispatchers.IO) {
        if (!networkService.isConnected()) {
            return@withContext Either.failure(Failure(ErrorType.LOST_INTERNET))
        }

        try {
            val response = goongApiService.getDirections(
                apiKey = BuildConfig.GOONG_API_KEY,
                origin = "${origin.latitude},${origin.longitude}",
                destination = "${destination.latitude},${destination.longitude}"
            )

            if (!response.isSuccessful) {
                return@withContext Either.failure(
                    Failure(
                        errorType = ErrorType.SERVER_RESPONSE_ERROR,
                        message = response.message(),
                        errorCode = response.code(),
                    )
                )
            }

            val routes = response.body()?.routes.orEmpty().mapIndexedNotNull { index, route ->
                val encodedPolyline = route.overviewPolyline?.points ?: return@mapIndexedNotNull null
                val distanceMeters = route.legs?.firstOrNull()?.distance?.value ?: 0
                val durationSeconds = route.legs?.firstOrNull()?.duration?.value ?: 0
                val isBestRoute = index == 0
                RouteOptionUiModel(
                    id = "route_$index",
                    encodedPolyline = encodedPolyline,
                    distanceMeters = distanceMeters,
                    durationSeconds = durationSeconds,
                    durationText = RouteFormatUtils.formatDuration(durationSeconds),
                    distanceText = RouteFormatUtils.formatDistance(distanceMeters),
                    label = if (isBestRoute) "Đường tốt nhất" else "Tuyến thay thế ${index + 1}",
                    isSelected = index == 0,
                    rawRoute = route,
                )
            }

            if (routes.isEmpty()) {
                Either.failure(Failure(ErrorType.SERVER_RESPONSE_ERROR, "No route found"))
            } else {
                Either.success(routes)
            }
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
