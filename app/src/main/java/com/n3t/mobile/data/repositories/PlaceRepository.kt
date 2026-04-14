package com.n3t.mobile.data.repositories

import com.n3t.mobile.core.services.NetworkService
import com.n3t.mobile.data.datasources.remote.N3TApiService
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.api_util.ErrorType
import com.n3t.mobile.data.model.api_util.Failure
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
    override suspend fun searchPlace(text: String, lat: Float?, lng: Float?): Either<Failure, PlaceResponseModel?> = Either.failure(Failure())
    override suspend fun getPlaceDetail(placeId: String): Either<Failure, PlaceDetailResponseModel?> = Either.failure(Failure())
}
