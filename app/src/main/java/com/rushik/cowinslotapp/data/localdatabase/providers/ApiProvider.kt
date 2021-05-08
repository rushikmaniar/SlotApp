package com.rushik.cowinslotapp.data.localdatabase.providers

import com.rushik.cowinslotapp.models.DistrictApiResponse
import com.rushik.cowinslotapp.models.SessionApiResponse
import com.rushik.cowinslotapp.models.StateApiResponse
import com.rushik.cowinslotapp.proxies.ApiClient

class ApiProvider {
    var metaApiProvider = MetaApiProvider()
    var slotAvailabilityApiProvider = SlotAvailabilityApiProvider()
}

class MetaApiProvider {
    suspend fun fetchStates(): StateApiResponse {
        return ApiClient.getApiInterface().getStates()
    }

    suspend fun fetchDistricts(stateId: String): DistrictApiResponse {
        return ApiClient.getApiInterface().getDistricts(stateId)
    }
}

class SlotAvailabilityApiProvider {
    suspend fun findByDistrict(districtId: String, date: String): SessionApiResponse {
        return ApiClient.getApiInterface().findByDistrict(districtId = districtId, date = date)
    }
}