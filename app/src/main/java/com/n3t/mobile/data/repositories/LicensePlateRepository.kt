package com.n3t.mobile.data.repositories

import com.n3t.mobile.core.services.NetworkService
import com.n3t.mobile.data.api.HandleApiResponse
import com.n3t.mobile.data.datasources.remote.N3TApiService
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.api_util.ErrorType
import com.n3t.mobile.data.model.api_util.Failure
import com.n3t.mobile.data.model.api_util.toApiResponseFail
import com.n3t.mobile.data.model.car.AddLicensePlateRequest
import com.n3t.mobile.data.model.car.LookupLicensePlateRequest
import com.n3t.mobile.data.model.car.RequestDeleteLicensePlate
import com.n3t.mobile.data.model.car.TrafficInfoModel
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface LicensePlateRepository {
    suspend fun licensePlateQuery(request: LookupLicensePlateRequest): Either<Failure, List<TrafficInfoModel>>
    suspend fun getLicensePlates(): Either<Failure, List<String>>
    suspend fun addLicensePlate(request: AddLicensePlateRequest): Either<Failure, JsonElement?>
    suspend fun deleteLicensePlate(request: RequestDeleteLicensePlate): Either<Failure, JsonElement?>
}

class LicensePlateRepositoryImpl(
    private val remoteDataSource: N3TApiService,
    private val networkService: NetworkService,
) : LicensePlateRepository {

    override suspend fun licensePlateQuery(request: LookupLicensePlateRequest): Either<Failure, List<TrafficInfoModel>> =
        withContext(Dispatchers.IO) {
            HandleApiResponse.whenInternetError(networkService)?.let { return@withContext it }
            try {
                val response = remoteDataSource.licensePlateQuery(request).execute()
                if (response.isSuccessful) {
                    response.body()?.let { return@withContext Either.success(it) }
                }
                val responseFail = response.toApiResponseFail()
                return@withContext Either.failure(
                    Failure(ErrorType.SERVER_RESPONSE_ERROR, responseFail?.message, responseFail?.code)
                )
            } catch (e: Exception) {
                return@withContext Either.failure(Failure(ErrorType.SERVER_ERROR, e.message))
            }
        }

    override suspend fun getLicensePlates(): Either<Failure, List<String>> =
        withContext(Dispatchers.IO) {
            HandleApiResponse.whenInternetError(networkService)?.let { return@withContext it }
            try {
                val response = remoteDataSource.getLicensePlates().execute()
                if (response.isSuccessful) {
                    response.body()?.let { return@withContext Either.success(it) }
                }
                val responseFail = response.toApiResponseFail()
                return@withContext Either.failure(
                    Failure(ErrorType.SERVER_RESPONSE_ERROR, responseFail?.message, responseFail?.code)
                )
            } catch (e: Exception) {
                return@withContext Either.failure(Failure(ErrorType.SERVER_ERROR, e.message))
            }
        }

    override suspend fun addLicensePlate(request: AddLicensePlateRequest): Either<Failure, JsonElement?> =
        withContext(Dispatchers.IO) {
            HandleApiResponse.whenInternetError(networkService)?.let { return@withContext it }
            try {
                val response = remoteDataSource.addLicensePlate(request).execute()
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

    override suspend fun deleteLicensePlate(request: RequestDeleteLicensePlate): Either<Failure, JsonElement?> =
        withContext(Dispatchers.IO) {
            HandleApiResponse.whenInternetError(networkService)?.let { return@withContext it }
            try {
                val response = remoteDataSource.deleteLicensePlate(request).execute()
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
