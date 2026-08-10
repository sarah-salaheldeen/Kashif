package com.example.kashifapp.place.data.remote

import com.example.kashifapp.core.data.util.safeCall
import com.example.kashifapp.core.domain.util.DataError
import com.example.kashifapp.core.domain.util.Result
import com.example.kashifapp.core.domain.util.map
import com.example.kashifapp.place.data.mapper.toOsmFilter
import com.example.kashifapp.place.data.mapper.toPlace
import com.example.kashifapp.place.domain.model.City
import com.example.kashifapp.place.domain.model.Place
import com.example.kashifapp.place.domain.model.PlaceCategory
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
        val filters = categories.joinToString("\n") { category ->
            val osmFilter = category.toOsmFilter()
            // Both node and way — node is a point, way is a building outline
            """
            node[$osmFilter](around:${city.radiusMeters},${city.latitude},${city.longitude});
            way[$osmFilter](around:${city.radiusMeters},${city.latitude},${city.longitude});
            """.trimIndent()
        }
        return """
            [out:json][timeout:30];
            ($filters);
            out body center 200;
        """.trimIndent()
    }
}