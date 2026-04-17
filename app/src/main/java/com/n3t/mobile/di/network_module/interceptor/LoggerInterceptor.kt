package com.n3t.mobile.di.network_module.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class LoggerInterceptor : Interceptor {
    companion object {
        private const val TAG = "N3T_HTTP"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.nanoTime()

        Log.d(TAG, "--> ${request.method} ${request.url}")

        val response = chain.proceed(request)

        val duration = (System.nanoTime() - startTime) / 1_000_000
        Log.d(TAG, "<-- ${response.code} ${request.url} (${duration}ms)")
        try {
            val responseBodyString = response.peekBody(1024 * 1024).string()
            val maxLogSize = 3000
            for (i in 0..responseBodyString.length / maxLogSize) {
                val start = i * maxLogSize
                var end = (i + 1) * maxLogSize
                end = if (end > responseBodyString.length) responseBodyString.length else end
                val chunk = responseBodyString.substring(start, end)
                if (chunk.isNotBlank()) {
                    Log.d(TAG, "<-- body: $chunk")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error reading response body", e)
        }

        return response
    }
}
