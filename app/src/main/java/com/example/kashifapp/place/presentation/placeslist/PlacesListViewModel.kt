package com.example.kashifapp.place.presentation.placeslist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kashifapp.R
import com.example.kashifapp.auth.domain.model.User
import com.example.kashifapp.core.data.location.LocationDataSource
import com.example.kashifapp.core.domain.model.UserLocation
import com.example.kashifapp.core.domain.util.DataError
import com.example.kashifapp.core.domain.util.Result
import com.example.kashifapp.core.presentation.util.UiText
import com.example.kashifapp.core.presentation.util.toUiText
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
    private val placeRepository: PlaceRepository,
    private val locationDataSource: LocationDataSource
): ViewModel() {

    private val _state = MutableStateFlow(PlacesListState())
    val state = _state.asStateFlow()

    private val _events = Channel<PlacesListEvent>()
    val events = _events.receiveAsFlow()

    // Called by the screen after permission result
    fun onAction(action: PlacesListAction) {
        when (action) {
            PlacesListAction.OnLocationPermissionGranted -> { loadLocation() }
            PlacesListAction.OnLocationPermissionDenied -> {
                _state.update {
                    it.copy(
                        locationStatus = LocationStatus.PermissionDenied,
                        //locationError = UiText.StringResourceId(R.string.error_location_permission)
                    )
                }
            }
            is PlacesListAction.OnCategorySelected -> {
                _state.update { it.copy(selectedCategory = action.category) }
            }
            is PlacesListAction.OnSearchQueryChanged -> {
                _state.update { it.copy(searchQuery = action.query) }
            }
            is PlacesListAction.OnPlaceClicked -> {
                viewModelScope.launch {
                    _events.send(PlacesListEvent.NavigateToDetail(action.place.id))
                }
            }
            is PlacesListAction.OnErrorDismissed -> {
                _state.update {
                    it.copy(
                        errorMessage = null,
                        locationStatus = LocationStatus.Idle
                    )
                }
            }
            is PlacesListAction.OnFavoriteClick -> {
                viewModelScope.launch {
                    placeRepository.toggleSaved(action.place.id, !action.place.isSaved)
                }
            }
            PlacesListAction.OnRefreshRequested -> loadLocation()
        }
    }

    private fun loadLocation() {
        viewModelScope.launch {
            _state.update { it.copy(locationStatus = LocationStatus.Loading) }
            when (val result = locationDataSource.getCurrentLocation()) {
                is Result.Success -> {
                    val location = result.data
                    _state.update {
                        it.copy(
                        userLocation = location,
                        locationStatus = LocationStatus.Success
                    )
                    }
                    observePlaces(location)
                    syncIfStale(location)
                }
                is Result.Error -> {
                    val status = when (result.error) {
                        DataError.Location.PERMISSION_DENIED -> LocationStatus.PermissionDenied
                        DataError.Location.UNAVAILABLE -> LocationStatus.Unavailable
                        DataError.Location.DISABLED -> LocationStatus.Disabled
                    }
                    _state.update { it.copy(locationStatus = status, isLoading = false) }
                }
            }
        }
    }

    private fun observePlaces(location: UserLocation) {
        viewModelScope.launch {
            combine(
                // Immediate restart when category changes
                _state.map { it.selectedCategory }.distinctUntilChanged(),
                // Debounced restart for search typing
                _state.map { it.searchQuery }.distinctUntilChanged().debounce(300L)
            ) { category, query -> category to query }
                .flatMapLatest { (category, query) ->
                    placeRepository.observePlaces(
                        location,
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

    private fun syncIfStale(location: UserLocation) {
        viewModelScope.launch {
            val lastSync = placeRepository.getLAstSyncTime(location.latitude, location.longitude)
            val isStale = lastSync == null ||
                    System.currentTimeMillis() - lastSync > SYNC_THRESHOLD_MS
            if (isStale) syncPlaces(location)
        }
    }

    private fun syncPlaces(location: UserLocation) {
        viewModelScope.launch {
            val showFullScreenLoading = _state.value.places.isEmpty()
            _state.update {
                it.copy(
                    isLoading = showFullScreenLoading,
                    isSyncing = !showFullScreenLoading,
                    errorMessage = null
                )
            }

            // One request for all categories — query is ~600 chars, well within limits
            val result = placeRepository.syncPlaces(
                location = location,
                categories = PlaceCategory.entries.filter { it != PlaceCategory.OTHER }
            )

            _state.update { it.copy(isLoading = false, isSyncing = false) }

            if (result is Result.Error && _state.value.places.isEmpty()) {
                _state.update { it.copy(errorMessage = result.error.toUiText()) }
            }
        }
    }

    companion object {
        private const val SYNC_THRESHOLD_MS = 24 * 60 * 60 * 1000L
    }
}