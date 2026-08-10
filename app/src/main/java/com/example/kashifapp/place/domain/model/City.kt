package com.example.kashifapp.place.domain.model

data class City(
    val id: String,
    val nameEn: String,
    val nameAr: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val radiusMeters: Int = 10_000
) {
    companion object {
        val KHARTOUM = City("khartoum", "Khartoum", "الخرطوم", "Sudan",        15.5007, 32.5599)
        val CAIRO    = City("cairo",    "Cairo",    "القاهرة", "Egypt",         30.0444, 31.2357, 15_000)
        val RIYADH   = City("riyadh",  "Riyadh",   "الرياض",  "Saudi Arabia",  24.6877, 46.7219, 15_000)
        val ALL      = listOf(KHARTOUM, CAIRO, RIYADH)
    }
}