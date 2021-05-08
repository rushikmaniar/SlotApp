package com.rushik.cowinslotapp.models

import com.google.gson.annotations.SerializedName

data class State(
    @SerializedName("state_id")
    var stateId: Int,
    @SerializedName("state_name")
    var stateName: String,
)
