package com.rushik.cowinslotapp.data.localdatabase.dao

import androidx.room.*

@Dao
interface BaseDao<T> {
    @Insert
    suspend fun insert(data: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMultipleReplace(data: ArrayList<T>)

    @Update
    suspend fun update(data: T)

    @Update
    suspend fun updateMultiple(data: ArrayList<T>)

    @Delete
    suspend fun delete(data: T)
}