package com.example.drivex.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.drivex.data.converterss.Converters
import com.example.drivex.data.model.MapModels
import com.example.drivex.domain.MapDao

@Database(
    entities = [MapModels::class],
    version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MapRoomDatabase : RoomDatabase() {

    abstract fun getmapDao(): MapDao
    companion object {

        @Volatile
        private var INSTANCE: MapRoomDatabase? = null

        fun getInstance(context: Context): MapRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MapRoomDatabase::class.java,
                    "map_table"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}