package com.example.drivex.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.drivex.data.model.Expenses
import com.example.drivex.domain.ExpensesDao

@Database(entities = [Expenses::class], version = 1, exportSchema = false)
abstract class ExpensesRoomDatabase : RoomDatabase() {
    abstract fun refuelDao(): ExpensesDao
    companion object {

        @Volatile
        private var INSTANCE: ExpensesRoomDatabase? = null

        fun getInstance(context: Context): ExpensesRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null)
            return tempInstance
                 synchronized(this) {
                     val instance = Room.databaseBuilder(
                         context.applicationContext,
                         ExpensesRoomDatabase::class.java,
                     "refuel database"
                     ).build()
                     INSTANCE = instance
                     return instance
                 }
        }
    }
}