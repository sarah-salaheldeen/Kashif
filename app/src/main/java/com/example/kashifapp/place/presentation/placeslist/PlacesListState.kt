package com.example.kashifapp.place.presentation.placeslist

import com.example.kashifapp.core.presentation.util.UiText
import com.example.kashifapp.place.domain.Place
import com.example.kashifapp.place.domain.PlaceCategory

//state is all values that could change over time and have impact on our ui (screen)
data class PlacesListState(
    val searchQuery: String = "",
    val searchResults: List<Place> = emptyList(),
    val isLoading: Boolean = true,
    val selectedCategory: PlaceCategory? = null,
    val errorMessage: UiText? = null
)