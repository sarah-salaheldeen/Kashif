package com.example.kashifapp.place.domain.repository

import com.example.kashifapp.core.domain.model.UserLocation
import com.example.kashifapp.core.domain.util.DataError
import com.example.kashifapp.core.domain.util.Result
import com.example.kashifapp.place.domain.model.City
import com.example.kashifapp.place.domain.model.Place
import com.example.kashifapp.place.domain.model.PlaceCategory
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {
    // Room-backed Flow — emits whenever local data changes
    fun observePlaces(
        location: UserLocation,
        category: PlaceCategory?,
        query: String = ""
    ): Flow<List<Place>>

    // Fetches from Overpass and writes to Room — Room Flow then emits automatically
    suspend fun syncPlaces(
        location: UserLocation,
        categories: List<PlaceCategory>
    ): Result<Unit, DataError.Remote>

    suspend fun toggleSaved(placeId: String, isSaved: Boolean)
    suspend fun getLAstSyncTime(latitude: Double, longitude: Double): Long?

}