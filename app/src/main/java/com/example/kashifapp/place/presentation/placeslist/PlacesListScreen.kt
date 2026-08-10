package com.example.kashifapp.place.presentation.placeslist

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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

@Composable
fun PlacesListScreenRoot(
    viewModel: PlacesListViewModel = hiltViewModel(),
    onPlaceClick: (String) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is PlacesListEvent.NavigateToDetail -> onPlaceClick(event.placeId)
            }
        }
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

            when {
                state.isLoading -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 64.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                state.places.isEmpty() -> {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 64.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_places_found),
                                color = ColorPrimaryText.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
                else -> {
                    items(
                        items = state.places,
                        key = { it.id }
                    ) { place ->
                        PlaceCard(
                            place = place,
                            onFavoriteClick = {
                                onAction(PlacesListAction.OnFavoriteClick(place))
                            },
                            onPlaceClick = {
                                onAction(PlacesListAction.OnPlaceClicked(place))
                            }
                        )
                    }
                }
            }
            // Bottom padding so last card clears the nav bar
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Preview(locale = "ar")
@Composable
fun PlacesListScreenPreview() {
    KashifAppTheme {
        /*PlacesListScreen()*/
    }
}