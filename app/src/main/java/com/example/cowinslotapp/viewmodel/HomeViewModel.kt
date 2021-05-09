package com.example.cowinslotapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.cowinslotapp.frameworks.AppCache

class HomeViewModel : ViewModel() {
    var isServiceRunningLiveData = AppCache.isServiceRunningLiveData
    var dateString = AppCache.selectedDateString
}