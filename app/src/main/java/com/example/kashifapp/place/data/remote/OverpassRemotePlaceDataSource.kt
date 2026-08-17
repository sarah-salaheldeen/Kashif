package com.example.kashifapp.place.data.remote

import com.example.kashifapp.core.data.util.safeCall
import com.example.kashifapp.core.domain.util.DataError
import com.example.kashifapp.core.domain.util.Result
import com.example.kashifapp.core.domain.util.map
import com.example.kashifapp.place.data.mapper.toGeoapifyCategory
import com.example.kashifapp.place.data.mapper.toPlace
import com.example.kashifapp.place.domain.model.City
import com.example.kashifapp.place.domain.model.Place
import com.example.kashifapp.place.domain.model.PlaceCategory
import java.util.Locale
import javax.inject.Inject

class OverpassRemotePlaceDataSource @Inject constructor(
    private val api: OverpassApiService
) {
    suspend fun fetchPlaces(
        city: City,
        categories: List<PlaceCategory>
    ): Result<List<Place>, DataError.Remote> {
        val query = buildQuery(city, categories)
        return safeCall { api.queryPlaces(query) }
            .map { response ->
                response.elements
                    .mapNotNull { it.toPlace(city.id) }
            }
    }

    private fun buildQuery(city: City, categories: List<PlaceCategory>): String {
        val filters = StringBuilder()
        categories.forEach { category ->
            val osmFilter = category.toGeoapifyCategory()
            if (osmFilter.isBlank()) return@forEach  // ← skip anything unmapped
            val around = String.format(
                Locale.US,
                "around:%d,%.6f,%.6f",
                city.radiusMeters,
                city.latitude,
                city.longitude
            )
            filters.append("node[$osmFilter]($around);")
            filters.append("way[$osmFilter]($around);")
        }
        return "[out:json][timeout:30];($filters);out body center 200;"
    }
}