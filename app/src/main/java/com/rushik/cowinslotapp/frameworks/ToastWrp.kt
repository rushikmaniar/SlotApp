package com.rushik.cowinslotapp.frameworks

import android.content.Context
import android.widget.Toast
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToastWrp {
    companion object {
        fun success(context: Context, msg: String, duration: Int = Toast.LENGTH_LONG) {
            CoroutineScope(Dispatchers.Main).launch {
                Toasty.success(context, msg, duration).show()
            }
        }

        fun error(context: Context, msg: String, duration: Int = Toast.LENGTH_LONG) {
            CoroutineScope(Dispatchers.Main).launch {
                Toasty.error(context, msg, duration).show()
            }
        }

        fun info(context: Context, msg: String, duration: Int = Toast.LENGTH_LONG) {
            CoroutineScope(Dispatchers.Main).launch {
                Toasty.info(context, msg, duration).show()
            }
        }

        fun warning(context: Context, msg: String, duration: Int = Toast.LENGTH_LONG) {
            CoroutineScope(Dispatchers.Main).launch {
                Toasty.warning(context, msg, duration).show()
            }
        }
    }
}