package com.example.cowinslotapp.proxies

import com.example.cowinslotapp.models.DistrictApiResponse
import com.example.cowinslotapp.models.SessionApiResponse
import com.example.cowinslotapp.models.StateApiResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json",
    )
    @GET("admin/location/states")
    suspend fun getStates(): StateApiResponse

    @Headers(
        "Content-Type: application/json",
        "Accept: application/json",
    )
    @GET("admin/location/districts/{stateId}")
    suspend fun getDistricts(@Path("stateId") stateId: String): DistrictApiResponse

    @Headers(
        "Content-Type: application/json",
        "Accept: application/json",
    )
    @GET("appointment/sessions/public/findByDistrict")
    suspend fun findByDistrict(
        @Query("district_id") districtId: String,
        @Query("date") date: String
    ): SessionApiResponse
}