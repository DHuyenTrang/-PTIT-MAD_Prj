package com.n3t.mobile.data.api

import android.content.Context
import android.content.SharedPreferences

enum class Flavor {
    STAGING,
    PRODUCTION
}

data class Env(
    val baseUrlApi: String,
    val baseUrlTracking: String,
    val baseUrlWebsocket: String,
    val baseUrlSearchMedia: String,
    val flavor: Flavor = Flavor.STAGING,
    val maxConnectTimeout: Long = 10,
)

class Environment {
    private var _type = Flavor.PRODUCTION
    val type: Flavor
        get() = _type

    private val staging = Env(
        baseUrlApi = "http://localhost/",
        baseUrlTracking = "http://localhost/",
        baseUrlWebsocket = "http://localhost/",
        baseUrlSearchMedia = "http://localhost/",
        flavor = Flavor.STAGING,
    )

    private val production = Env(
        baseUrlApi = "http://localhost/",
        baseUrlTracking = "http://localhost/",
        baseUrlWebsocket = "http://localhost/",
        baseUrlSearchMedia = "http://localhost/",
        flavor = Flavor.PRODUCTION,
    )

    private fun env(flavorType: Flavor) = when (flavorType) {
        Flavor.STAGING -> staging
        Flavor.PRODUCTION -> production
    }

    val baseUrlApi: String
        get() = env(_type).baseUrlApi

    val baseUrlTracking: String
        get() = env(_type).baseUrlTracking

    val baseUrlWebsocket: String
        get() = env(_type).baseUrlWebsocket

    val baseUrlSearchMedia: String
        get() = env(_type).baseUrlSearchMedia

    val maxConnectTimeout: Long
        get() = env(_type).maxConnectTimeout

    companion object {
        private var _instance: Environment? = null
        val instance: Environment
            get() {
                _instance = _instance ?: Environment()
                return _instance!!
            }

        fun setFlavor(flavor: Flavor) {
            instance._type = flavor
        }

        fun loadFlavor(context: Context): Flavor {
            return instance.loadFlavor(context)
        }

        fun saveFlavor(context: Context, flavor: Flavor) {
            instance.saveFlavor(context, flavor)
        }
    }

    private fun loadFlavor(context: Context): Flavor {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("n3t", Context.MODE_PRIVATE)

        return when (sharedPreferences.getString("env", null)) {
            "s" -> Flavor.STAGING
            "p" -> Flavor.PRODUCTION
            else -> Flavor.PRODUCTION
        }
    }

    private fun saveFlavor(context: Context, flavor: Flavor) {
        try {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("n3t", Context.MODE_PRIVATE)
            val name = when (flavor) {
                Flavor.STAGING -> "s"
                Flavor.PRODUCTION -> "p"
            }
            sharedPreferences.edit().putString("env", name).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
