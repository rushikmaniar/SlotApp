package com.rushik.cowinslotapp.services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.rushik.cowinslotapp.frameworks.AppCache
import com.rushik.cowinslotapp.frameworks.AppLog
import java.text.SimpleDateFormat
import java.util.*

class SlotCheckService : Service() {
    val handler = Handler(Looper.getMainLooper())

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        checkSlotsRunnable.run()

        return START_STICKY
    }

    private val checkSlotsRunnable = object : Runnable {
        override fun run() {
            val str = """
                state : ${AppCache.selectedState.value?.stateName}
                district : ${AppCache.selectedDistrict.value?.districtName}
                Date : ${AppCache.selectedDateString.value}
                CurrentDate : ${SimpleDateFormat("yyyy-mm-dd hh:mm:ss a",Locale.US).format(Date())}
            """.trimIndent()

            AppLog.debug(str,"AppLog")

            handler.postDelayed(this, AppCache.checkSlotsInterval)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(checkSlotsRunnable)
    }
}