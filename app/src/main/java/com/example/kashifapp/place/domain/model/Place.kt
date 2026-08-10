package com.example.kashifapp.place.domain.model

data class Place(
    val id: String, // Composite key built in mapper: "{type}/{numericId}" e.g. "node/361342838"
    // Overpass returns the numeric id as Long — node and way IDs
    // are independent sequences in OSM and can collide, so the
    // composite is the true unique key. See OsmElementDto.toPlace().
    val lat: Double,
    val lon: Double,
    val nameAr: String?,
    val nameEn: String,
    val name: String,
    val placeDetails: PlaceDetails?,
    val category: PlaceCategory,
    val city: String,
    val isSaved: Boolean = false,
    val lastSyncedAt: Long = 0L
)

fun Place.displayName(isArabic: Boolean): String =
    if (isArabic && !nameAr.isNullOrBlank()) nameAr else nameEn