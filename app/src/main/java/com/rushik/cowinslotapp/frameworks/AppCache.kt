package com.rushik.cowinslotapp.frameworks

import androidx.lifecycle.MutableLiveData
import com.rushik.cowinslotapp.models.District
import com.rushik.cowinslotapp.models.State

object AppCache {
    var statesLiveData = MutableLiveData<List<State>>(arrayListOf())
    var districtLiveData = MutableLiveData<List<District>>(arrayListOf())
    var selectedState = MutableLiveData<State?>(null)
    var selectedDistrict = MutableLiveData<District?>(null)
    var selectedDateString = MutableLiveData<String?>(null)
    var isServiceRunningLiveData = MutableLiveData(false)
    var checkSlotsIntervalLiveData = MutableLiveData(AppConstant.DEFAULT_CHECK_SLOT_INTERVAL)
    var knownCenters = arrayListOf<Int>()
    var timeIntervals = arrayListOf(2,5,10)
    var isNetworkAvailableLiveData = MutableLiveData(false)
}