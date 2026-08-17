package com.example.kashifapp.place.presentation.placeslist

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kashifapp.R
import com.example.kashifapp.core.presentation.components.PlaceChipsList
import com.example.kashifapp.core.presentation.components.TopBar
import com.example.kashifapp.place.domain.model.PlaceCategory
import com.example.kashifapp.place.presentation.placeslist.components.PlaceCard
import com.example.kashifapp.place.presentation.placeslist.components.SearchBar
import com.example.kashifapp.ui.theme.BackgroundColor
import com.example.kashifapp.ui.theme.ChipBackgroundSelected
import com.example.kashifapp.ui.theme.ColorPrimaryText
import com.example.kashifapp.ui.theme.KashifAppTheme
import com.mapbox.base.common.logger.model.Message

@Composable
fun PlacesListScreenRoot(
    viewModel: PlacesListViewModel = hiltViewModel(),
    onPlaceClick: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Request permission on first launch
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            viewModel.onAction(PlacesListAction.OnLocationPermissionGranted)
        }else {
            viewModel.onAction(PlacesListAction.OnLocationPermissionDenied)
        }
    }

    // Trigger permission request when screen first appears and re-check on resume
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // If location was disabled or permission was denied, retry when coming back
                val currentStatus = state.locationStatus
                if (currentStatus == LocationStatus.Disabled ||
                    currentStatus == LocationStatus.PermissionDenied
                ) {
                    viewModel.onAction(PlacesListAction.OnRefreshRequested)
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        if (state.locationStatus == LocationStatus.Idle) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
        viewModel.events.collect { event ->
            when (event) {
                is PlacesListEvent.NavigateToDetail -> onPlaceClick(event.placeId)
            }
        }
    }

    // Handle GPS disabled — open location settings
    if (state.locationStatus == LocationStatus.Disabled) {
        LocationDisabledDialog(
            onOpenSettings = {
                context.startActivity(
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                )
            },
            onDismiss = {
                viewModel.onAction(PlacesListAction.OnErrorDismissed)
            }
        )
    }

    PlacesListScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun PlacesListScreen(
    state: PlacesListState,
    onAction: (PlacesListAction) -> Unit,
   modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar()

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            item {
                SearchBar(
                    query = state.searchQuery,
                    onQueryChanged = { onAction(PlacesListAction.OnSearchQueryChanged(it)) },
                )
            }
            item {
                PlaceChipsList(
                    placeCategories = PlaceCategory.entries,
                    selectedCategory = state.selectedCategory,
                    onCategorySelected = { onAction(PlacesListAction.OnCategorySelected(it)) },
                    modifier = Modifier
                        .padding(bottom = 24.dp)
                )
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.recommended_places),
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically),
                        color = ColorPrimaryText,
                        fontSize = 20.sp
                    )
                    Text(
                        text = stringResource(R.string.see_all),
                        modifier = Modifier
                            .align(Alignment.CenterVertically),
                        color = ChipBackgroundSelected,
                        fontSize = 14.sp
                    )
                }
            }

            // ── Content states ────────────────────────────────────────────
            when {

                // Location permission denied
                state.locationStatus == LocationStatus.PermissionDenied -> {
                    item(key = "permission_denied") {
                        LocationErrorView(
                            icon     = R.drawable.ic_location_off,
                            message     = stringResource(R.string.error_location_permission),
                            actionLabel = stringResource(R.string.open_app_settings),
                            onAction    = {
                                val intent = Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                ).apply {
                                    data = Uri.fromParts(
                                        "package",
                                        context.packageName,
                                        null
                                    )
                                }
                                context.startActivity(intent)
                            }
                        )
                    }
                }

                // Location unavailable (GPS on but no fix)
                state.locationStatus == LocationStatus.Unavailable -> {
                    item(key = "location_unavailable") {
                        LocationErrorView(
                            icon     = R.drawable.ic_location_off,
                            message     = stringResource(R.string.error_location_unavailable),
                            actionLabel = stringResource(R.string.retry),
                            onAction    = { onAction(PlacesListAction.OnRefreshRequested) }
                        )
                    }
                }

                // Full-screen loading (Room empty + first sync)
                state.isLoading -> {
                    item(key = "loading") {
                        Box(
                            modifier        = Modifier
                                .fillMaxWidth()
                                .padding(top = 80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = ChipBackgroundSelected
                            )
                        }
                    }
                }

                // Network or sync error (no cached data to fall back to)
                state.errorMessage != null -> {
                    item(key = "error") {
                        LocationErrorView(
                            icon     = R.drawable.ic_error,
                            message     = state.errorMessage.asString(),
                            actionLabel = stringResource(R.string.retry),
                            onAction    = { onAction(PlacesListAction.OnRefreshRequested) }
                        )
                    }
                }

                // No places found (search or category filter returned nothing)
                state.locationStatus == LocationStatus.Success
                        && state.places.isEmpty()
                        && !state.isSyncing -> {
                    item(key = "empty") {
                        EmptyPlacesView(
                            hasActiveFilter = state.selectedCategory != null
                                    || state.searchQuery.isNotBlank(),
                            onClearFilter   = {
                                onAction(PlacesListAction.OnCategorySelected(null))
                                onAction(PlacesListAction.OnSearchQueryChanged(""))
                            }
                        )
                    }
                }

                // Places list — the happy path
                else -> {
                    items(
                        items = state.places,
                        key   = { it.id }
                    ) { place ->
                        PlaceCard(
                            place          = place,
                            onFavoriteClick = {
                                onAction(PlacesListAction.OnFavoriteClick(place))
                            },
                            onPlaceClick   = {
                                onAction(PlacesListAction.OnPlaceClicked(place))
                            },
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical   = 8.dp
                            )
                        )
                    }
                }
            }
            // Bottom padding so last card clears the nav bar
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun LocationDisabledDialog(
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                painter = painterResource(R.drawable.ic_location_off),
                contentDescription = null
            )
        },
        title = {
            Text(stringResource(R.string.location_disabled_title))
        },
        text = {
            Text(stringResource(R.string.location_disabled_message))
        },
        confirmButton = {
            TextButton(onClick = onOpenSettings) {
                Text(stringResource(R.string.open_settings))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
private fun LocationErrorView(
    icon: Int,
    message: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = ColorPrimaryText.copy(alpha = 0.4f),
            modifier = Modifier.size(52.dp)
        )
        Text(
            text = message,
            color = ColorPrimaryText.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
        Button(onClick = onAction) {
            Text(actionLabel)
        }
    }
}

@Composable
private fun EmptyPlacesView(
    hasActiveFilter: Boolean,
    onClearFilter: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_explore_outlined),
            contentDescription = null,
            tint = ColorPrimaryText.copy(alpha = 0.35f),
            modifier = Modifier.size(56.dp)
        )
        Text(
            text      = stringResource(
                if (hasActiveFilter) R.string.no_places_for_filter
                else R.string.no_places_nearby
            ),
            color     = ColorPrimaryText.copy(alpha = 0.6f),
            textAlign = TextAlign.Center,
            style     = MaterialTheme.typography.bodyMedium
        )
        if (hasActiveFilter) {
            TextButton(onClick = onClearFilter) {
                Text(
                    text = stringResource(R.string.clear_filters),
                    color = ChipBackgroundSelected
                )
            }
        }
    }
}

@Preview(locale = "ar")
@Composable
fun PlacesListScreenPreview() {
    KashifAppTheme {
        PlacesListScreen(PlacesListState(), {})
    }
}