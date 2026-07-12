package com.example.kashifapp.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    //Auth Graph
    @Serializable data object AuthGraph: Route
    @Serializable data object Login: Route
    @Serializable data object Register: Route

    //Main Graph
    @Serializable data object MainGraph: Route
    @Serializable data object Discover: Route
    @Serializable data class PlaceDetail(val placeId: String): Route
    @Serializable data object MoodSearch: Route
    @Serializable data object SavedPlaces: Route
    @Serializable data object Settings: Route
}