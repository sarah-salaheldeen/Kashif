package com.example.kashifapp.place.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [PlaceEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(MapTypeConverter::class)
abstract class KashifDatabase: RoomDatabase() {
    abstract fun placeDao(): PlaceDao
}