package com.rushik.cowinslotapp.data.localdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rushik.cowinslotapp.frameworks.AppConstant
import com.rushik.cowinslotapp.data.localdatabase.dao.SubscriptionDao
import com.rushik.cowinslotapp.data.localdatabase.entities.Subscription

@Database(
    entities = [
        Subscription::class,
    ], version = 9, exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun subscriptionDao(): SubscriptionDao

    companion object {

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, AppConstant.DATABASE_NAME)
                .fallbackToDestructiveMigration().build()
        }
    }

}