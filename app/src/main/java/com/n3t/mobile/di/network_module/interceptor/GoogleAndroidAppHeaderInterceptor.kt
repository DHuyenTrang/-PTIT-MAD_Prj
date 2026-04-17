package com.n3t.mobile.di.network_module.interceptor

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import java.security.MessageDigest
import java.util.Locale

/**
 * Adds Android app identity headers so Google Web Service APIs can validate
 * API keys restricted by Android package + certificate SHA-1.
 */
class GoogleAndroidAppHeaderInterceptor(
    private val context: Context,
) : Interceptor {

    private val packageNameValue: String by lazy { context.packageName }
    private val certSha1Value: String? by lazy { readCertificateSha1() }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val certSha1 = certSha1Value

        val nextRequest = if (certSha1.isNullOrBlank()) {
            request
        } else {
            request.newBuilder()
                .header("X-Android-Package", packageNameValue)
                .header("X-Android-Cert", certSha1)
                .build()
        }

        return chain.proceed(nextRequest)
    }

    private fun readCertificateSha1(): String? {
        return try {
            val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                context.packageManager.getPackageInfo(
                    packageNameValue,
                    PackageManager.GET_SIGNING_CERTIFICATES,
                )
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(
                    packageNameValue,
                    PackageManager.GET_SIGNATURES,
                )
            }

            val signatureBytes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo.signingInfo?.apkContentsSigners?.firstOrNull()?.toByteArray()
            } else {
                @Suppress("DEPRECATION")
                packageInfo.signatures?.firstOrNull()?.toByteArray()
            } ?: return null

            sha1(signatureBytes)
        } catch (_: Exception) {
            null
        }
    }

    private fun sha1(input: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-1").digest(input)
        return digest.joinToString(separator = ":") { byte ->
            String.format(Locale.US, "%02X", byte)
        }
    }
}
