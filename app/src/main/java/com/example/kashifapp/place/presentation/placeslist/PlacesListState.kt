package com.example.kashifapp.place.presentation.placeslist

import com.example.kashifapp.core.domain.model.UserLocation
import com.example.kashifapp.core.presentation.util.UiText
import com.example.kashifapp.place.domain.model.City
import com.example.kashifapp.place.domain.model.Place
import com.example.kashifapp.place.domain.model.PlaceCategory

//state is all values that could change over time and have impact on our ui (screen)
data class PlacesListState(
    val userLocation: UserLocation? = null, // null until permission granted
    val locationStatus: LocationStatus = LocationStatus.Idle,
    val selectedCategory: PlaceCategory? = null,   // null = All
    val searchQuery: String = "",
    val places: List<Place> = emptyList(),
    val isLoading: Boolean = false,     // true when Room is empty + first fetch
    val isSyncing: Boolean = false,     // true during background refresh
    val errorMessage: UiText? = null
)

sealed interface LocationStatus {
    data object Idle: LocationStatus
    data object Loading: LocationStatus
    data object Success: LocationStatus
    data object PermissionDenied: LocationStatus
    data object Disabled: LocationStatus
    data object Unavailable: LocationStatus
}