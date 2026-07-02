package com.hecos.mobile.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PendingSyncBatch::class], version = 1, exportSchema = false)
abstract class HecosDatabase : RoomDatabase() {

    abstract fun pendingSyncBatchDao(): PendingSyncBatchDao

    companion object {
        @Volatile
        private var instance: HecosDatabase? = null

        fun getInstance(context: Context): HecosDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    HecosDatabase::class.java,
                    "hecos.db"
                ).build().also { instance = it }
            }
        }
    }
}
