package com.rushik.cowinslotapp.viewmodel

import android.view.View
import androidx.databinding.BindingAdapter

class BindingAdapter {
    companion object {
        @JvmStatic
        @BindingAdapter("app:isEnabled")
        fun isVisible(view: View, isEnabled: Boolean) {
            view.isEnabled = isEnabled
        }
    }
}