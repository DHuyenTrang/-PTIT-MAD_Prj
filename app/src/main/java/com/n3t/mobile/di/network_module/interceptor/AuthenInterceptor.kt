package com.n3t.mobile.di.network_module.interceptor

import android.util.Log
import com.n3t.mobile.BuildConfig
import com.n3t.mobile.data.api.ApiEndPoint
import com.n3t.mobile.data.api.Environment
import com.n3t.mobile.data.datasources.local.AppStore
import com.n3t.mobile.data.model.authen.refresh_token.RefreshTokenResponse
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response

class AuthenInterceptor(
    private val appStore: AppStore,
) : Interceptor {

    companion object {
        private const val TAG = "AuthenInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val headers = Headers.Builder()
            .add("Content-Type", "application/json")
            .add("Accept", "application/json")

        val accessToken = appStore.getAccessToken()
        if (accessToken != null) {
            headers.add("Authorization", "Bearer $accessToken")
        }

        val userId = appStore.getCustomerId()
        if (userId != -1) {
            headers.add("X-Id", userId.toString())
        }

        headers.add("app-version", BuildConfig.VERSION_NAME)

        val request = chain.request()
            .newBuilder()
            .headers(headers.build())
            .build()

        val response = chain.proceed(request)

        // Token expired — need refresh
        if (response.code == 401) {
            val newAccessToken = processRefreshToken(response, chain, accessToken)

            if (newAccessToken != null) {
                // Retry request with new token
                val newHeaders = Headers.Builder()
                newHeaders["Accept"] = "application/json"
                newHeaders["Content-Type"] = "application/json"
                newHeaders["Authorization"] = "Bearer $newAccessToken"
                if (userId != -1) {
                    newHeaders.add("X-Id", userId.toString())
                }
                newHeaders.add("app-version", BuildConfig.VERSION_NAME)

                val retryRequest = chain.request().newBuilder()
                    .headers(newHeaders.build())
                    .build()
                return chain.proceed(retryRequest)
            }

            // Refresh failed — return original 401 response
            Log.w(TAG, "Token refresh failed, returning 401")
        }

        return response
    }

    private fun processRefreshToken(
        response: Response,
        chain: Interceptor.Chain,
        accessToken: String?,
    ): String? {
        response.close()
        Log.d(TAG, "Token expired, refreshing...")

        val refreshToken = appStore.getRefreshToken() ?: return null

        val requestBody = FormBody.Builder()
            .add("refresh_token", refreshToken)
            .build()

        val newHeaders = Headers.Builder()
        newHeaders["Accept"] = "application/json"
        newHeaders["Content-Type"] = "application/json"
        if (accessToken != null) {
            newHeaders["Authorization"] = "Bearer $accessToken"
        }

        val userId = appStore.getCustomerId()
        if (userId != -1) {
            newHeaders.add("X-Id", userId.toString())
        }

        val baseUrl = Environment.instance.baseUrlApi
        val newRequest = chain.request().newBuilder()
            .url(baseUrl + ApiEndPoint.REFRESH_TOKEN.substring(1))
            .post(requestBody)
            .headers(newHeaders.build())
            .build()

        val refreshTokenResponse = chain.proceed(newRequest)

        if (refreshTokenResponse.code == 200) {
            val responseBody = refreshTokenResponse.body?.string()
            responseBody?.let {
                try {
                    val gson = Gson()
                    val res = gson.fromJson(responseBody, RefreshTokenResponse::class.java)

                    val newAccessToken = res.accessToken
                    if (newAccessToken != null) {
                        appStore.setAccessToken(newAccessToken)
                    }
                    if (res.refreshToken != null) {
                        appStore.setRefreshToken(res.refreshToken)
                    }
                    Log.d(TAG, "Token refreshed successfully")
                    return newAccessToken
                } catch (e: Exception) {
                    Log.e(TAG, "Error parsing refresh token response: ${e.message}")
                }
            }
        }

        return null
    }
}
