package com.example.kashifapp.place.presentation.placeslist

import com.example.kashifapp.place.domain.Place
import com.example.kashifapp.place.domain.PlaceCategory

sealed interface PlacesListAction {
    data class OnSearchQueryChange(val query: String): PlacesListAction
    data class OnPlaceClick(val place: Place): PlacesListAction
    data class OnCategorySelected(val category: PlaceCategory): PlacesListAction
    data object OnFavoriteClick: PlacesListAction
}