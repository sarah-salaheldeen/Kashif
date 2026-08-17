package com.example.kashifapp.place.data.remote

import android.util.Log
import com.example.kashifapp.core.data.util.safeCall
import com.example.kashifapp.core.domain.model.UserLocation
import com.example.kashifapp.core.domain.util.DataError
import com.example.kashifapp.core.domain.util.Result
import com.example.kashifapp.core.domain.util.map
import com.example.kashifapp.di.GeoapifyApiKey
import com.example.kashifapp.place.data.mapper.toGeoapifyCategory
import com.example.kashifapp.place.data.mapper.toPlace
import com.example.kashifapp.place.domain.model.City
import com.example.kashifapp.place.domain.model.Place
import com.example.kashifapp.place.domain.model.PlaceCategory
import javax.inject.Inject

class GeoapifyRemotePlaceDataSource @Inject constructor(
    private val api: GeoapifyApiService,
    @GeoapifyApiKey private val apiKey: String
) {
    suspend fun fetchPlaces(
        location: UserLocation,
        categories: List<PlaceCategory>
    ): Result<List<Place>, DataError.Remote> {
        Log.d("GeoapifyRemotePlaceDataSource", "fetchPlaces :) api key is ==>> $apiKey")
        val categoryString = categories
            .map { it.toGeoapifyCategory() }
            .filter { it.isNotBlank() }
            .joinToString(",")

        if (categoryString.isBlank()) {
            return Result.Error(DataError.Remote.UNKNOWN)
        }

        return safeCall {
            api.getPlaces(
                categories = categoryString,
                filter     = "circle:${location.longitude},${location.latitude},${location.radiusMeters}",
                bias       = "proximity:${location.longitude},${location.latitude}",
                limit      = 50,
                apiKey     = apiKey
            )
        }.map { response ->
            response.features.mapNotNull { it.toPlace() }
        }
    }
}