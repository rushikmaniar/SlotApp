package com.example.cowinslotapp.frameworks

import androidx.lifecycle.MutableLiveData
import com.example.cowinslotapp.models.District
import com.example.cowinslotapp.models.State
import java.text.SimpleDateFormat
import java.util.*

object AppCache {
    var statesLiveData = MutableLiveData<List<State>>(arrayListOf())
    var districtLiveData = MutableLiveData<List<District>>(arrayListOf())
    var selectedState = MutableLiveData<State?>(null)
    var selectedDistrict = MutableLiveData<District?>(null)
    var selectedDateString = MutableLiveData(SimpleDateFormat(AppConstant.DEFAULT_DATE_FORMAT, Locale.US ).format(Date()))
    var isServiceRunningLiveData = MutableLiveData(false)
    var checkSlotsIntervalLiveData = MutableLiveData(AppConstant.DEFAULT_CHECK_SLOT_INTERVAL)
    var knownCenters = arrayListOf<Int>()
    var timeIntervals = arrayListOf(20,30,60,120)
    var isNetworkAvailableLiveData = MutableLiveData(false)
}