package com.n3t.mobile.data.repositories

import com.n3t.mobile.core.services.NetworkService
import com.n3t.mobile.data.api.HandleApiResponse
import com.n3t.mobile.data.datasources.remote.N3TApiService
import com.n3t.mobile.data.model.api_util.Either
import com.n3t.mobile.data.model.api_util.ErrorType
import com.n3t.mobile.data.model.api_util.Failure
import com.n3t.mobile.data.model.api_util.toApiResponseFail
import com.n3t.mobile.data.model.authen.LogoutResponse
import com.google.gson.JsonElement
import com.n3t.mobile.data.model.authen.login.*
import com.n3t.mobile.data.model.authen.refresh_token.RefreshTokenRequest
import com.n3t.mobile.data.model.authen.refresh_token.RefreshTokenResponse
import com.n3t.mobile.data.model.authen.verify_otp.VerifyOTPData
import com.n3t.mobile.data.model.authen.verify_otp.VerifyOTPRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface AuthenRepository {
    suspend fun checkPhoneExist(phone: String): Either<Failure, GetAccountStatusResponse>
    suspend fun login(data: LoginRequest): Either<Failure, LoginData?>
    suspend fun loginWithPassword(data: LoginWithPasswordRequest): Either<Failure, LoginWithPasswordResponse?>
    suspend fun resendOtp(data: ReSendOtpRequest): Either<Failure, ReSendOtpResponse?>
    suspend fun verifyOTP(data: VerifyOTPRequest): Either<Failure, VerifyOTPData?>
    suspend fun refreshToken(data: RefreshTokenRequest): Either<Failure, RefreshTokenResponse?>
    suspend fun forgotPassword(data: PasswordForgotRequest): Either<Failure, PasswordForgotResponse?>
    suspend fun updatePassword(data: PasswordUpdateRequest): Either<Failure, PasswordUpdateResponse?>
    suspend fun logout(): Either<Failure, LogoutResponse?>
    suspend fun getUserProfile(): Either<Failure, JsonElement>
}

class AuthenRepositoryImpl(
    private val remoteDataSource: N3TApiService,
    private val networkService: NetworkService,
) : AuthenRepository {

    override suspend fun checkPhoneExist(phone: String): Either<Failure, GetAccountStatusResponse> =
        withContext(Dispatchers.IO) {
            HandleApiResponse.whenInternetError(networkService)?.let {
                return@withContext it
            }
            try {
                val data = GetAccountStatus(phone)
                val response = remoteDataSource.checkPhoneExist(data).execute()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        return@withContext Either.success(body)
                    }
                }
                val responseFail = response.toApiResponseFail()?.message
                return@withContext Either.failure(
                    Failure(ErrorType.SERVER_RESPONSE_ERROR)
                )
            } catch (e: Exception) {
                return@withContext Either.failure(Failure(ErrorType.SERVER_ERROR))
            }
        }

    override suspend fun login(data: LoginRequest): Either<Failure, LoginData?> =
        withContext(Dispatchers.IO) {
            HandleApiResponse.whenInternetError(networkService)?.let {
                return@withContext it
            }
            try {
                val response = remoteDataSource.login(data).execute()
                return@withContext HandleApiResponse.processResponseData(response)
            } catch (e: Exception) {
                return@withContext Either.failure(Failure(ErrorType.SERVER_ERROR))
            }
        }

    override suspend fun loginWithPassword(data: LoginWithPasswordRequest): Either<Failure, LoginWithPasswordResponse?> =
        withContext(Dispatchers.IO) {
            HandleApiResponse.whenInternetError(networkService)?.let {
                return@withContext it
            }
            try {
                val response = remoteDataSource.loginWithPassword(data).execute()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        return@withContext Either.success(body)
                    }
                }
                val responseFail = response.toApiResponseFail()?.message
                return@withContext Either.failure(
                    Failure(ErrorType.SERVER_RESPONSE_ERROR)
                )
            } catch (e: Exception) {
                return@withContext Either.failure(Failure(ErrorType.SERVER_ERROR))
            }
        }

    override suspend fun resendOtp(data: ReSendOtpRequest): Either<Failure, ReSendOtpResponse?> =
        withContext(Dispatchers.IO) {
            HandleApiResponse.whenInternetError(networkService)?.let {
                return@withContext it
            }
            try {
                val response = remoteDataSource.reSendOtp(data).execute()
                if (response.isSuccessful) {
                    response.body()?.let { return@withContext Either.success(it) }
                }
                val responseFail = response.toApiResponseFail()?.message
                return@withContext Either.failure(
                    Failure(ErrorType.SERVER_RESPONSE_ERROR)
                )
            } catch (e: Exception) {
                return@withContext Either.failure(Failure(ErrorType.SERVER_ERROR))
            }
        }

    override suspend fun verifyOTP(data: VerifyOTPRequest): Either<Failure, VerifyOTPData?> =
        withContext(Dispatchers.IO) {
            HandleApiResponse.whenInternetError(networkService)?.let { return@withContext it }
            try {
                val response = remoteDataSource.verifyOTP(data).execute()
                if (response.isSuccessful) {
                    response.body()?.let { return@withContext Either.success(it) }
                }
                val responseFail = response.toApiResponseFail()?.message
                return@withContext Either.failure(
                    Failure(ErrorType.SERVER_RESPONSE_ERROR)
                )
            } catch (e: Exception) {
                return@withContext Either.failure(Failure(ErrorType.SERVER_ERROR))
            }
        }

    override suspend fun refreshToken(data: RefreshTokenRequest): Either<Failure, RefreshTokenResponse?> =
        withContext(Dispatchers.IO) {
            HandleApiResponse.whenInternetError(networkService)?.let { return@withContext it }
            try {
                val response = remoteDataSource.refreshToken(data).execute()
                if (response.isSuccessful) {
                    response.body()?.let { return@withContext Either.success(it) }
                }
                val responseFail = response.toApiResponseFail()?.message
                return@withContext Either.failure(
                    Failure(ErrorType.SERVER_RESPONSE_ERROR)
                )
            } catch (e: Exception) {
                return@withContext Either.failure(Failure(ErrorType.SERVER_ERROR))
            }
        }

    override suspend fun forgotPassword(data: PasswordForgotRequest): Either<Failure, PasswordForgotResponse?> =
        withContext(Dispatchers.IO) {
            HandleApiResponse.whenInternetError(networkService)?.let { return@withContext it }
            try {
                val response = remoteDataSource.forgotPassword(data).execute()
                if (response.isSuccessful) {
                    response.body()?.let { return@withContext Either.success(it) }
                }
                val responseFail = response.toApiResponseFail()?.message
                return@withContext Either.failure(
                    Failure(ErrorType.SERVER_RESPONSE_ERROR)
                )
            } catch (e: Exception) {
                return@withContext Either.failure(Failure(ErrorType.SERVER_ERROR))
            }
        }

    override suspend fun updatePassword(data: PasswordUpdateRequest): Either<Failure, PasswordUpdateResponse?> =
        withContext(Dispatchers.IO) {
            HandleApiResponse.whenInternetError(networkService)?.let { return@withContext it }
            try {
                val response = remoteDataSource.updatePassword(data).execute()
                if (response.isSuccessful) {
                    response.body()?.let { return@withContext Either.success(it) }
                }
                val responseFail = response.toApiResponseFail()?.message
                return@withContext Either.failure(
                    Failure(ErrorType.SERVER_RESPONSE_ERROR)
                )
            } catch (e: Exception) {
                return@withContext Either.failure(Failure(ErrorType.SERVER_ERROR))
            }
        }

    override suspend fun logout(): Either<Failure, LogoutResponse?> =
        withContext(Dispatchers.IO) {
            HandleApiResponse.whenInternetError(networkService)?.let { return@withContext it }
            try {
                val response = remoteDataSource.logout().execute()
                if (response.isSuccessful) {
                    response.body()?.let { return@withContext Either.success(it) }
                }
                val responseFail = response.toApiResponseFail()?.message
                return@withContext Either.failure(
                    Failure(ErrorType.SERVER_RESPONSE_ERROR)
                )
            } catch (e: Exception) {
                return@withContext Either.failure(Failure(ErrorType.SERVER_ERROR))
            }
        }

    override suspend fun getUserProfile(): Either<Failure, JsonElement> =
        withContext(Dispatchers.IO) {
            HandleApiResponse.whenInternetError(networkService)?.let { return@withContext it }
            try {
                val response = remoteDataSource.getUserProfile().execute()
                if (response.isSuccessful) {
                    response.body()?.let { return@withContext Either.success(it) }
                        ?: return@withContext Either.failure(Failure(ErrorType.SERVER_RESPONSE_ERROR))
                }
                return@withContext Either.failure(Failure(ErrorType.SERVER_RESPONSE_ERROR))
            } catch (e: Exception) {
                return@withContext Either.failure(Failure(ErrorType.SERVER_ERROR))
            }
        }
}


