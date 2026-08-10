package com.example.kashifapp.place.presentation.placeslist

import com.example.kashifapp.core.presentation.util.UiText
import com.example.kashifapp.place.domain.model.City
import com.example.kashifapp.place.domain.model.Place
import com.example.kashifapp.place.domain.model.PlaceCategory

//state is all values that could change over time and have impact on our ui (screen)
data class PlacesListState(
    val selectedCity: City = City.KHARTOUM,
    val selectedCategory: PlaceCategory? = null,   // null = All
    val searchQuery: String = "",
    val places: List<Place> = emptyList(),
    val isLoading: Boolean = false,     // true when Room is empty + first fetch
    val isSyncing: Boolean = false,     // true during background refresh
    val errorMessage: UiText? = null
)