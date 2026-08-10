package com.example.kashifapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.kashifapp.auth.domain.repository.AuthRepository
import com.example.kashifapp.core.presentation.components.KashifBottomNav
import com.example.kashifapp.place.presentation.placeslist.PlacesListScreen
import com.example.kashifapp.place.presentation.placeslist.PlacesListScreenRoot
import com.example.kashifapp.ui.theme.BackgroundColor

@Composable
fun AppNavigation(
    authRepository: AuthRepository
) {
    val navController = rememberNavController()

    // Determine start destination once — don't use a Flow here, just
    // a one-time check to avoid auth flash on startup
    val startDestination = remember {
        if (authRepository.isLoggedIn()) Route.MainGraph
        else Route.AuthGraph
    }

    // Observe the current back stack entry reactively
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    // Only show bottom nav on top-level screens
    // Place detail, for example, should not show it
    val showBottomNav = AppDestinations.topLevelDestinations.any { destination ->
        currentDestination?.hierarchy
            ?.any { it.hasRoute(destination.route::class) }
            ?: false
    }

    Scaffold(
        containerColor = BackgroundColor,
        bottomBar = {
            if (showBottomNav) {
                KashifBottomNav(
                    destinations = AppDestinations.topLevelDestinations,
                    isSelected = { destination ->
                        currentDestination?.hierarchy
                            ?.any { it.hasRoute(destination.route::class) }
                            ?: false
                    },
                    onDestinationClick = { destination ->
                        navController.navigate(destination.route) {
                            // Pop up to the start destination to avoid
                            // building up a large back stack
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination
                            launchSingleTop = true
                            // Restore state when re-selecting a tab
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            // ── Auth graph ──────────────────────────────────────────
            navigation<Route.AuthGraph>(startDestination = Route.Login) {
                composable<Route.Login> {
                    /*LoginScreenRoot(
                        onLoginSuccess = {
                            navController.navigate(Route.MainGraph) {
                                popUpTo(Route.AuthGraph) { inclusive = true }
                            }
                        },
                        onNavigateToRegister = {
                            navController.navigate(Route.Register)
                        }
                    )*/
                }
                composable<Route.Register> {
                    /*RegisterScreenRoot(
                        onRegisterSuccess = {
                            navController.navigate(Route.MainGraph) {
                                popUpTo(Route.AuthGraph) { inclusive = true }
                            }
                        },
                        onNavigateToLogin = { navController.popBackStack() }
                    )*/
                }
            }
            // ── Main graph ──────────────────────────────────────────
            navigation<Route.MainGraph>(startDestination = Route.Discover) {
                composable<Route.Discover> {
                    PlacesListScreenRoot(
                        onPlaceClick = { placeId ->
                            navController.navigate(Route.PlaceDetail(placeId))
                        }
                    )
                }
                composable<Route.PlaceDetail> { backStackEntry ->
                    val route = backStackEntry.toRoute<Route.PlaceDetail>()
                    /*PlaceDetailScreenRoot(
                        placeId = route.placeId,
                        onBack = { navController.popBackStack() }
                    )*/
                }
                composable<Route.MoodSearch> {
                    /*MoodSearchScreenRoot(
                        onPlaceClick = { placeId ->
                            navController.navigate(Route.PlaceDetail(placeId))
                        }
                    )*/
                }
                composable<Route.SavedPlaces> {
                    /*SavedPlacesScreenRoot(
                        onPlaceClick = { placeId ->
                            navController.navigate(Route.PlaceDetail(placeId))
                        }
                    )*/
                }
                composable<Route.Settings> {
                    //SettingsScreenRoot()
                }
            }
        }
    }
}