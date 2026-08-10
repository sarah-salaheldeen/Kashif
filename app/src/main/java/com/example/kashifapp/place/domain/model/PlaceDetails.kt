package com.example.kashifapp.place.domain.model

data class PlaceDetails(
    val address: String?= null,
    val phone: String?= null,
    val webSite: String?= null,
    val cuisine: String? = null,
    val openingHours: String? = null,
    val description: String? = null,
    val capacity: Int? = null,
    val extraInfo: Map<String, String>,
) {
    // Used by Mood Search keyword ranking.
    // Combines all predefined text fields + extra tag values into
    // a flat list of lowercase tokens for keyword matching.
    fun searchableKeywords(): List<String> {
        val named = listOfNotNull(cuisine, description, address)
        val extra = extraInfo.values.toList()
        return (named + extra)
            .flatMap { it.split(",", ";", " ") }
            .map { it.trim().lowercase() }
            .filter { it.isNotBlank() }
    }
}