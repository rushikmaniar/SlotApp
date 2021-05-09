package com.example.cowinslotapp

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import com.example.cowinslotapp.frameworks.AppCache
import com.example.cowinslotapp.frameworks.AppHelper

class BaseApplication : Application() {
    private lateinit var connectivityManager: ConnectivityManager

    override fun onCreate() {
        super.onCreate()
        connectivityManager = getSystemService(ConnectivityManager::class.java)

        registerNetworkListener()
    }

    private fun registerNetworkListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    AppCache.isNetworkAvailableLiveData.postValue(true)
                }

                override fun onLost(network: Network) {
                    AppCache.isNetworkAvailableLiveData.postValue(false)
                    if (AppCache.isServiceRunningLiveData.value == true) {
                        AppHelper.showNotification(
                            applicationContext, 123457,
                            getString(R.string.network_error),
                            getString(R.string.message_check_your_connection),
                        )
                    }
                }
            })
        }
    }
}