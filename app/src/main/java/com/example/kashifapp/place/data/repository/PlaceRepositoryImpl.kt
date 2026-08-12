package com.example.kashifapp.place.data.repository

import android.util.Log
import com.example.kashifapp.core.domain.util.DataError
import com.example.kashifapp.core.domain.util.Result
import com.example.kashifapp.core.domain.util.map
import com.example.kashifapp.place.data.local.PlaceDao
import com.example.kashifapp.place.data.mapper.toPlace
import com.example.kashifapp.place.data.mapper.toPlaceEntity
import com.example.kashifapp.place.data.remote.FirestoreSavedPlacesDataSource
import com.example.kashifapp.place.data.remote.OverpassRemotePlaceDataSource
import com.example.kashifapp.place.domain.model.City
import com.example.kashifapp.place.domain.model.Place
import com.example.kashifapp.place.domain.model.PlaceCategory
import com.example.kashifapp.place.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlaceRepositoryImpl @Inject constructor(
    private val placeDao: PlaceDao,
    private val remoteDataSource: OverpassRemotePlaceDataSource,
    private val firestoreSavedPlaces: FirestoreSavedPlacesDataSource

): PlaceRepository {
    override fun observePlaces(
        cityId: String,
        category: PlaceCategory?,
        query: String
    ): Flow<List<Place>> {
        return placeDao
            .observePlaces(
                cityId = cityId,
                category = category?.name,
                query = query
            )
            .map { entities -> entities.map { it.toPlace() } }
    }

    override suspend fun syncPlaces(
        city: City,
        categories: List<PlaceCategory>
    ): Result<Unit, DataError.Remote> {
        return remoteDataSource
            .fetchPlaces(city, categories)
            .map { places ->
                // Write to Room — the observePlaces Flow emits automatically after this
                placeDao.upsertAll(places.map { place -> place.toPlaceEntity() })
            }
    }

    override suspend fun toggleSaved(placeId: String, isSaved: Boolean) {
        // Always update Room first (offline-first)
        placeDao.updateSavedStatus(placeId, isSaved)
        // Then sync to Firestore in the background — failure is silent,
        // Room is source of truth, Firestore is backup
        try {
            if (isSaved) firestoreSavedPlaces.savePlaceRemote(placeId)
            else firestoreSavedPlaces.removeSavedPlaceRemote(placeId)
        } catch (e: Exception) {
            // Log but don't surface to user — local state is already correct
            Log.d("PlaceRepositoryImpl", "toggleSaved: ${e.message}")
        }
    }

    override suspend fun getLAstSyncTime(cityId: String): Long? {
        return placeDao.getLastSynTime(cityId)
    }

}