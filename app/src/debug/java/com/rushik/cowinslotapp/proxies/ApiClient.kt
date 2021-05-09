package com.rushik.cowinslotapp.proxies

import com.facebook.flipper.plugins.network.FlipperOkhttpInterceptor
import com.facebook.flipper.plugins.network.NetworkFlipperPlugin
import com.rushik.cowinslotapp.frameworks.AppHelper
import com.rushik.cowinslotapp.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
    companion object {

        private fun getRetrofitBuilder(): Retrofit? {
            return try {
                val oktHttpClient = OkHttpClient.Builder()
                oktHttpClient.addInterceptor {
                    it.proceed(
                        it.request().newBuilder().addHeader("User-Agent", "PostmanRuntime/7.28.0").build()
                    )
                }
                oktHttpClient.addNetworkInterceptor(FlipperOkhttpInterceptor(getNetworkFlipperPlugin()))

                Retrofit.Builder()
                    .baseUrl(AppHelper.withTrailingSlash(BuildConfig.BASE_URL))
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(oktHttpClient.build())
                    .build()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

        fun getApiInterface(): ApiInterface {
            return getRetrofitBuilder()?.create(ApiInterface::class.java)!!
        }

        private var networkFlipperPlugin: NetworkFlipperPlugin? = null

        fun getNetworkFlipperPlugin(): NetworkFlipperPlugin {
            if (networkFlipperPlugin == null) {
                networkFlipperPlugin = NetworkFlipperPlugin()
            }
            return networkFlipperPlugin as NetworkFlipperPlugin
        }
    }
}