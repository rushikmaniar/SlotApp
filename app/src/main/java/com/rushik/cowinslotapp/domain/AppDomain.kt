package com.rushik.cowinslotapp.domain

import com.rushik.cowinslotapp.data.localdatabase.providers.ApiProvider
import com.rushik.cowinslotapp.frameworks.AppCache

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
}