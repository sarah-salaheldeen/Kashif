package com.example.kashifapp.navigation

import androidx.annotation.StringRes

data class TopLevelDestination(
    val route: Any,
    val iconRes: Int,
    val iconSelectedRes: Int,
    @StringRes val labelRes: Int
)
