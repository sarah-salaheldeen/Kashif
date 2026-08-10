package com.example.kashifapp.place.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "places")
data class PlaceEntity(
    @PrimaryKey val id: String,
    val nameEn: String,
    val nameAr: String,
    val category: String,       // PlaceCategory.name — stored as plain string
    val address: String?,
    val phone: String?,
    val website: String?,
    val openingHours: String?,
    val cuisine: String?,
    val description: String?,
    val capacity: Int?,
    val extraTagsJson: String,  // Map<String, String> serialized as JSON
    val lat: Double,
    val long: Double,
    val city: String,
    val isSaved: Boolean = false,
    val lastSyncedAt: Long = 0L
)
