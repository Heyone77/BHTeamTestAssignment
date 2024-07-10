package com.example.server.di

import android.content.Context
import androidx.room.Room
import com.example.server.data.AppDatabase
import com.example.server.data.TouchDataDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideTouchDataDao(database: AppDatabase): TouchDataDao {
        return database.touchDataDao()
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context
}
