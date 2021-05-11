package com.example.drivex.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.drivex.data.model.Refuel

@Database(entities = [Refuel::class], version = 1, exportSchema = false)
abstract class RefuelRoomDatabase : RoomDatabase() {
    abstract fun refuelDao(): RefuelDao
    companion object {

        @Volatile
        private var INSTANCE: RefuelRoomDatabase? = null

        fun getInstance(context: Context): RefuelRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null)
            return tempInstance
                 synchronized(this) {
                     val instance = Room.databaseBuilder(
                         context.applicationContext,
                         RefuelRoomDatabase::class.java,
                     "refuel database"
                     ).build()
                     INSTANCE = instance
                     return instance
                 }
        }
    }
}