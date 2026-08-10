package com.example.kashifapp.place.presentation.placeslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kashifapp.core.domain.util.Result
import com.example.kashifapp.core.presentation.toUiText
import com.example.kashifapp.place.domain.model.PlaceCategory
import com.example.kashifapp.place.domain.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacesListViewModel @Inject constructor(
    private val placeRepository: PlaceRepository
): ViewModel() {

    private val _state = MutableStateFlow(PlacesListState())
    val state = _state.asStateFlow()

    private val _events = Channel<PlacesListEvent>()
    val events = _events.receiveAsFlow()

    init {
        observePlaces()
        syncIfStale()
    }

    fun onAction(action: PlacesListAction) {
        when (action) {
            is PlacesListAction.OnCategorySelected -> {
                _state.update { it.copy(selectedCategory = action.category) }
            }
            is PlacesListAction.OnSearchQueryChanged -> {
                _state.update { it.copy(searchQuery = action.query) }
            }
            is PlacesListAction.OnCityChanged -> {
                _state.update { it.copy(selectedCity = action.city) }
                // New city — sync immediately
                syncPlaces()
            }
            is PlacesListAction.OnPlaceClicked -> {
                viewModelScope.launch {
                    _events.send(PlacesListEvent.NavigateToDetail(action.place.id))
                }
            }
            is PlacesListAction.OnErrorDismissed -> {
                _state.update { it.copy(errorMessage = null) }
            }
            is PlacesListAction.OnFavoriteClick -> {
                viewModelScope.launch {
                    placeRepository.toggleSaved(action.place.id, !action.place.isSaved)
                }
            }
            PlacesListAction.OnRefreshRequested -> syncPlaces()
        }
    }

    private fun observePlaces() {
        viewModelScope.launch {
            combine(
                // Immediate restart when city or category changes
                _state
                    .map { it.selectedCity to it.selectedCategory}
                    .distinctUntilChanged(),
                // Debounced restart for search typing
                _state
                .map { it.searchQuery }
                .distinctUntilChanged()
                    .debounce(300L)
            ) { (city, category), query ->
                Triple(city, category, query)
            }
                .flatMapLatest { (city, category, query) ->
                    placeRepository.observePlaces(
                        cityId = city.id,
                        category = category,
                        query = query
                    )
                }
                .collect { places ->
                    _state.update {
                        it.copy(
                            places = places,
                            isLoading = false // Room emitted — loading is done
                    ) }
                }
        }
    }

    private fun syncIfStale() {
        viewModelScope.launch {
            val lastSync = placeRepository.getLAstSyncTime(_state.value.selectedCity.id)
            val isStale = lastSync == null ||
                    System.currentTimeMillis() - lastSync > SYNC_THRESHOLD_MS
            if (isStale) syncPlaces()
        }
    }

    private fun syncPlaces() {
        viewModelScope.launch {
            // Only show full-screen loading if Room has nothing to show yet
            val showFullScreenLoading = _state.value.places.isEmpty()
            _state.update {
                it.copy(
                    isLoading = showFullScreenLoading,
                    isSyncing = !showFullScreenLoading,
                    errorMessage = null
                )
            }

            val result = placeRepository.syncPlaces(
                city = _state.value.selectedCity,
                categories = PlaceCategory.entries.toList()
            )

            _state.update { it.copy(isLoading = false, isSyncing = false) }

            // Only surface the error if we have nothing to show.
            // If we have cached data, silently fail — the user already sees places.
            if (result is Result.Error && _state.value.places.isEmpty()) {
                _state.update { it.copy(errorMessage = result.error.toUiText()) }
            }
        }
    }

    companion object {
        private const val SYNC_THRESHOLD_MS = 24 * 60 * 60 * 1000L
    }
}