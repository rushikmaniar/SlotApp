package com.example.cowinslotapp.services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.example.cowinslotapp.data.localdatabase.providers.ApiProvider
import com.example.cowinslotapp.domain.AppDomain
import com.example.cowinslotapp.frameworks.AppCache
import com.example.cowinslotapp.frameworks.AppConstant
import com.example.cowinslotapp.frameworks.AppHelper
import com.example.cowinslotapp.frameworks.AppLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SlotCheckService : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private val appDomain = AppDomain(ApiProvider())
    private var notificationId = 0

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        checkSlotsRunnable.run()
        startForeground(123456, AppHelper.getNotification(this))
        return START_STICKY
    }

    private val checkSlotsRunnable = object : Runnable {
        override fun run() {
            val str = """
                state : ${AppCache.selectedState.value?.stateName}
                district : ${AppCache.selectedDistrict.value?.districtName}
                Date : ${AppCache.selectedDateString.value}
                CurrentDate : ${SimpleDateFormat("yyyy-mm-dd hh:mm:ss a", Locale.US).format(Date())}
            """.trimIndent()

            AppLog.debug(str, "AppLog")

            checkForAvailableSlots()

            val interval = AppCache.checkSlotsIntervalLiveData.value ?: AppConstant.DEFAULT_CHECK_SLOT_INTERVAL
            handler.postDelayed(this, (interval * 60 * 1000L))
        }
    }

    fun checkForAvailableSlots() {
        CoroutineScope(Dispatchers.IO).launch {
            val response = appDomain.fetchAvailableSlotsByDistrict()
            val newCenters = response.filter { !AppCache.knownCenters.contains(it.centerId) }
            if (newCenters.isNotEmpty()) {
                val description = newCenters.joinToString(separator = ",") { it.name }
                AppHelper.showNotification(applicationContext, notificationId++, "New Slots Available", description)
            }
            AppCache.knownCenters = response.map { it.centerId }.toCollection(arrayListOf())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AppCache.knownCenters = arrayListOf()
        handler.removeCallbacks(checkSlotsRunnable)
    }
}