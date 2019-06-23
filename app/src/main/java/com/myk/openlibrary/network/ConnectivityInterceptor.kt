package com.myk.openlibrary.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import com.myk.openlibrary.NoInternetException

interface ConnectivityInterceptor : Interceptor

// this class allows us to impose our own constraints on network requests
class ConnectivityInterceptorImpl(private val context: Context) : ConnectivityInterceptor {

    // intercet the request and check constraints (for now we only ensure that internet connectivity is present)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!hasInternet()) throw NoInternetException()
        return chain.proceed(chain.request())
    }

    // return true if wifi or cellular data is available
    private fun hasInternet(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // fall back for earlier versions
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false

            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
                NetworkCapabilities.TRANSPORT_CELLULAR
            )
        } else {
            val mobileConnectivity = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            val wifiConnectivity = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

            if (mobileConnectivity == null && wifiConnectivity == null) return false
            return mobileConnectivity.isConnected || wifiConnectivity.isConnected
        }
    }
}