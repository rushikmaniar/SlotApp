package com.example.cowinslotapp.data.localdatabase.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cowinslotapp.data.localdatabase.entities.Subscription

@Dao
interface SubscriptionDao : BaseDao<Subscription> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(data: Subscription)
}