package com.rushik.cowinslotapp.frameworks

import android.util.Log

class AppLog {
    companion object {
        fun debug(msg: String, tag: String = AppConstant.DEBUG_TAG) {
            Log.d(tag, msg)
        }

        fun error(msg: String, tag: String = AppConstant.ERROR_TAG) {
            Log.e(AppConstant.ERROR_TAG, msg)
        }
    }
}