package com.n3t.mobile.data.datasources.remote

import com.n3t.mobile.data.api.ApiEndPoint
import com.n3t.mobile.data.model.api_util.ApiResponse
import com.n3t.mobile.data.model.authen.LogoutResponse
import com.n3t.mobile.data.model.authen.login.*
import com.n3t.mobile.data.model.authen.refresh_token.RefreshTokenRequest
import com.n3t.mobile.data.model.authen.refresh_token.RefreshTokenResponse
import com.n3t.mobile.data.model.authen.verify_otp.VerifyOTPData
import com.n3t.mobile.data.model.authen.verify_otp.VerifyOTPRequest
import com.n3t.mobile.data.model.car.AddLicensePlateRequest
import com.n3t.mobile.data.model.car.LookupLicensePlateRequest
import com.n3t.mobile.data.model.car.RequestDeleteLicensePlate
import com.n3t.mobile.data.model.car.TrafficInfoModel
import com.n3t.mobile.data.model.search_map.PlaceDetailResponseModel
import com.n3t.mobile.data.model.search_map.PlaceResponseModel
import com.n3t.mobile.data.model.search_map.SearchRouteRequest
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface N3TApiService {

    // ====== AUTH ======

    @POST(ApiEndPoint.CHECK_EMAIL_EXIST)
    fun checkEmailExist(@Body data: GetAccountStatus): Call<GetAccountStatusResponse>

    @POST(ApiEndPoint.LOGIN_WITH_PASSWORD)
    fun loginWithPassword(@Body data: LoginWithPasswordRequest): Call<LoginWithPasswordResponse>

    @POST(ApiEndPoint.RESEND_OTP)
    fun reSendOtp(@Body data: ReSendOtpRequest): Call<ReSendOtpResponse>

    @POST(ApiEndPoint.PASSWORD_FORGOT)
    fun forgotPassword(@Body data: PasswordForgotRequest): Call<PasswordForgotResponse>

    @POST(ApiEndPoint.PASSWORD_UPDATE)
    fun updatePassword(@Body data: PasswordUpdateRequest): Call<PasswordUpdateResponse>

    @POST(ApiEndPoint.VERIFY_OTP)
    fun verifyOTP(@Body body: VerifyOTPRequest): Call<VerifyOTPData>

    @POST(ApiEndPoint.REGISTER)
    fun register(@Body body: VerifyOTPRequest): Call<ApiResponse<VerifyOTPData>>

    @POST(ApiEndPoint.REFRESH_TOKEN)
    fun refreshToken(@Body body: RefreshTokenRequest): Call<RefreshTokenResponse>

    @POST(ApiEndPoint.LOGOUT)
    fun logout(): Call<LogoutResponse>

    // ====== LICENSE PLATE ======
    @POST(ApiEndPoint.CAR_ADD)
    fun addLicensePlate(@Body body: AddLicensePlateRequest): Call<JsonElement>

    @POST(ApiEndPoint.LICENSE_PLATE_QUERY)
    fun licensePlateQuery(@Body body: LookupLicensePlateRequest): Call<List<TrafficInfoModel>>

    @GET(ApiEndPoint.GET_LICENSE_PLATES)
    fun getLicensePlates(): Call<List<String>>

    @POST(ApiEndPoint.DELETE_LICENSE_PLATE)
    fun deleteLicensePlate(@Body body: RequestDeleteLicensePlate): Call<JsonElement>


    // ====== PROFILE ======
    @GET(ApiEndPoint.USER_PROFILE_APP)
    fun getUserProfile(): Call<JsonElement>
}
