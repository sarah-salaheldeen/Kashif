package com.example.kashifapp.core.domain.model

data class UserLocation(
    val latitude: Double,
    val longitude: Double,
    val radiusMeters: Int = 5000
) {
    // Rough bounding box — good enough for local discovery
    private val degreesLat get() = radiusMeters / 111_000.0
    private val degreesLon get() = radiusMeters / (111_000.0 * Math.cos(Math.toRadians(latitude)))

    val minLat get() = latitude - degreesLat
    val maxLat get() = latitude + degreesLat
    val minLon get() = longitude - degreesLon
    val maxLon get() = longitude + degreesLon
}
