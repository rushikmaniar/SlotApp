package com.example.cowinslotapp.models

import com.google.gson.annotations.SerializedName

data class District(
    @SerializedName("district_id")
    var districtId: Int,
    @SerializedName("district_name")
    var districtName: String,
)
