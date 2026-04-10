package com.n3t.mobile.core.services

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities

interface NetworkService {
    fun isConnected(): Boolean
    fun netWorkInfoChanged(callback: (isConnected: Boolean) -> Unit)
}

class NetworkServiceImpl(context: Context) : NetworkService {
    private val _context = context

    override fun isConnected(): Boolean {
        val manager =
            _context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val network: Network? = manager?.activeNetwork
        val capabilities = manager?.getNetworkCapabilities(network) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    override fun netWorkInfoChanged(callback: (isConnected: Boolean) -> Unit) {
        val connectivityManager =
            _context.getSystemService(ConnectivityManager::class.java)

        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                callback(true)
            }

            override fun onLost(network: Network) {
                callback(false)
            }
        })
    }
}
