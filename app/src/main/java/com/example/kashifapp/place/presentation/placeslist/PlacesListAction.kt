package com.example.kashifapp.place.presentation.placeslist

import com.example.kashifapp.place.domain.model.City
import com.example.kashifapp.place.domain.model.Place
import com.example.kashifapp.place.domain.model.PlaceCategory

sealed interface PlacesListAction {
    data class OnCategorySelected(val category: PlaceCategory?) : PlacesListAction
    data class OnSearchQueryChanged(val query: String) : PlacesListAction
    data class OnCityChanged(val city: City) : PlacesListAction
    data class OnPlaceClicked(val place: Place) : PlacesListAction
    data object OnRefreshRequested : PlacesListAction
    data object OnErrorDismissed : PlacesListAction
    data class OnFavoriteClick(val place: Place): PlacesListAction
}

sealed interface PlacesListEvent {
    data class NavigateToDetail(val placeId: String) : PlacesListEvent
}