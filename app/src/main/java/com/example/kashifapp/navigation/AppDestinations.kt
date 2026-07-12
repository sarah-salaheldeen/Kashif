package com.example.kashifapp.navigation

import com.example.kashifapp.R

object AppDestinations {
    val topLevelDestinations = listOf(
        TopLevelDestination(
            route = Route.Discover,
            iconRes = R.drawable.ic_discover,
            iconSelectedRes = R.drawable.ic_discover,
            labelRes = R.string.discover
        ),
        TopLevelDestination(
            route = Route.MoodSearch,
            iconRes = R.drawable.ic_mood,
            iconSelectedRes = R.drawable.ic_mood,
            labelRes = R.string.mood_search
        ),
        TopLevelDestination(
            route = Route.SavedPlaces,
            iconRes = R.drawable.ic_saved,
            iconSelectedRes = R.drawable.ic_saved,
            labelRes = R.string.saved_places
        ),
        TopLevelDestination(
            route = Route.Settings,
            iconRes = R.drawable.ic_settings,
            iconSelectedRes = R.drawable.ic_settings,
            labelRes = R.string.settings
        )
    )
}