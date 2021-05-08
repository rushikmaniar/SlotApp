package com.rushik.cowinslotapp.models

import com.google.gson.annotations.SerializedName

data class Session(
    @SerializedName("session_id")
    var sessionId:String,
    @SerializedName("center_id")
    var centerId: Int,
    var name: String,
    var address: String,
    @SerializedName("state_name")
    var stateName: String,
    @SerializedName("district_name")
    var districtName: String,
    @SerializedName("district_name")
    var blockName: String,
    var pincode: Int,
    var from: String,
    var to: String,
    @SerializedName("fee_type")
    var feeType: String,
    @SerializedName("min_age_limit")
    var minAgeLimit: String,
    var vaccine: String,
    var slots: ArrayList<String>,
)
