package com.rushik.cowinslotapp

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import com.facebook.flipper.android.AndroidFlipperClient
import com.facebook.flipper.android.utils.FlipperUtils
import com.facebook.flipper.plugins.crashreporter.CrashReporterPlugin
import com.facebook.flipper.plugins.databases.DatabasesFlipperPlugin
import com.facebook.flipper.plugins.inspector.DescriptorMapping
import com.facebook.flipper.plugins.inspector.InspectorFlipperPlugin
import com.facebook.flipper.plugins.leakcanary2.FlipperLeakListener
import com.facebook.flipper.plugins.leakcanary2.LeakCanary2FlipperPlugin
import com.facebook.flipper.plugins.sharedpreferences.SharedPreferencesFlipperPlugin
import com.facebook.soloader.SoLoader
import com.rushik.cowinslotapp.frameworks.AppCache
import com.rushik.cowinslotapp.frameworks.AppConstant
import com.rushik.cowinslotapp.frameworks.AppHelper
import com.rushik.cowinslotapp.proxies.ApiClient
import leakcanary.LeakCanary

class BaseApplication : Application() {
    private lateinit var connectivityManager: ConnectivityManager

    override fun onCreate() {
        super.onCreate()
        connectivityManager = getSystemService(ConnectivityManager::class.java)
        SoLoader.init(this, false)

        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
            LeakCanary.config = LeakCanary.config.copy(
                onHeapAnalyzedListener = FlipperLeakListener()
            )

            val client = AndroidFlipperClient.getInstance(this)
            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
            client.addPlugin(DatabasesFlipperPlugin(applicationContext));
            client.addPlugin(SharedPreferencesFlipperPlugin(applicationContext, AppConstant.APP_PREF))
            client.addPlugin(CrashReporterPlugin.getInstance());
            client.addPlugin(ApiClient.getNetworkFlipperPlugin())
            client.addPlugin(LeakCanary2FlipperPlugin())
            client.start()
        }

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