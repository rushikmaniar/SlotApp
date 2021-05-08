package com.rushik.cowinslotapp

import android.app.Application
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
import com.rushik.cowinslotapp.proxies.ApiClient
import leakcanary.LeakCanary

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
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
    }
}