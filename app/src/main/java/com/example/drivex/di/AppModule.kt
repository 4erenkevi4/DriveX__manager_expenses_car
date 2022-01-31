package com.example.drivex.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.example.drivex.domain.MapDao
import com.example.drivex.data.MapRoomDatabase
import com.example.drivex.utils.Constans.DATABASE_NAME
import com.example.drivex.utils.Constans.KEY_FIRST_TIME_TOGGLE
import com.example.drivex.utils.Constans.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

/**
 * AppModule, provides application wide singletons
 */
@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDb(app: Application): MapRoomDatabase {
        return Room.databaseBuilder(app, MapRoomDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideRunDao(db: MapRoomDatabase): MapDao {
        return db.getmapDao()
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(app: Application) =
        app.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)!!

    @Singleton
    @Provides
    fun provideFirstTimeToggle(sharedPreferences: SharedPreferences) = sharedPreferences.getBoolean(
        KEY_FIRST_TIME_TOGGLE, true
    )


}