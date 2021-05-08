package com.rushik.cowinslotapp.data.localdatabase.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["date","districtId"], unique = true)])
data class Subscription(
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,
    var date: String,
    var districtId: String
)