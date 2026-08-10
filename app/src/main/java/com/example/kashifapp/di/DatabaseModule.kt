package com.example.kashifapp.di

import android.content.Context
import androidx.room.Room
import com.example.kashifapp.place.data.local.KashifDatabase
import com.example.kashifapp.place.data.local.PlaceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideKashifDatabase(@ApplicationContext context: Context): KashifDatabase =
        Room.databaseBuilder(context, KashifDatabase::class.java, "kashif.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providePlaceDao(db: KashifDatabase): PlaceDao = db.placeDao()
}