package com.example.kashifapp.place.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {

    @Query("""
        SELECT * FROM places
        WHERE lat BETWEEN :minLat AND :maxLat
        AND long BETWEEN :minLon AND :maxLon
        AND (:category IS NULL OR category = :category)
        AND (:query = '' OR nameEn LIKE '%' || :query || '%' OR nameAr LIKE '%' || :query || '%')
        ORDER BY nameEn Asc
    """)
    fun observePlaces(
        minLat: Double,
        maxLat: Double,
        minLon: Double,
        maxLon: Double,
        category: String?,
        query: String
    ): Flow<List<PlaceEntity>>

    @Query("SELECT * FROM places WHERE isSaved = 1")
    fun observeSavedPlaces(): Flow<List<PlaceEntity>>

    @Query("SELECT * FROM places WHERE id = :id")
    suspend fun getPlaceById(id: String): PlaceEntity?

    // Preserves isSaved flag on update — don't overwrite user's saved state with remote data
    @Transaction
    suspend fun upsertAll(places: List<PlaceEntity>) {
        places.forEach { incoming ->
            val existing = getPlaceById(incoming.id)
            insertOrReplace(incoming.copy(isSaved = existing?.isSaved ?: false))
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(place: PlaceEntity)

    @Query("UPDATE places SET isSaved = :isSaved WHERE id = :id")
    suspend fun updateSavedStatus(id: String, isSaved: Boolean)

    @Query("SELECT MAX(lastSyncedAt) FROM places WHERE lat BETWEEN :minLat AND :maxLat AND long BETWEEN :minLon AND :maxLon")
    suspend fun getLastSynTime(
        minLat: Double,
        maxLat: Double,
        minLon: Double,
        maxLon: Double
    ): Long?
}