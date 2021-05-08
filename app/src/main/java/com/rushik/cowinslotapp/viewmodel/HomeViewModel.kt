package com.rushik.cowinslotapp.viewmodel

import androidx.lifecycle.ViewModel
import com.rushik.cowinslotapp.frameworks.AppCache

class HomeViewModel : ViewModel() {
    var isServiceRunningLiveData = AppCache.isServiceRunningLiveData
}