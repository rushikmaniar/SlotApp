package com.rushik.cowinslotapp.domain

import com.rushik.cowinslotapp.data.localdatabase.providers.ApiProvider
import com.rushik.cowinslotapp.frameworks.AppCache
import com.rushik.cowinslotapp.models.Session

class AppDomain(
    var apiProvider: ApiProvider
) {
    suspend fun fetchAndSetStates() {
        val response = apiProvider.metaApiProvider.fetchStates()
        AppCache.statesLiveData.postValue(response.states)
    }

    suspend fun fetchAndSetDistricts(stateId: String) {
        val response = apiProvider.metaApiProvider.fetchDistricts(stateId = stateId)
        AppCache.districtLiveData.postValue(response.districts)
    }

    suspend fun fetchAvailableSlotsByDistrict(): List<Session> {
        val date = AppCache.selectedDateString.value ?: return arrayListOf()
        val districtId = AppCache.selectedDistrict.value ?: return arrayListOf()

        val response = apiProvider.slotAvailabilityApiProvider.findByDistrict(districtId = districtId.districtId.toString(),date = date)
        return response.sessions
    }
}