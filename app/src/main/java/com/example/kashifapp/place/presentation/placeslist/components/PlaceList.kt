package com.example.kashifapp.place.presentation.placeslist.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kashifapp.R
import com.example.kashifapp.place.domain.Place

@Composable
fun PlaceList(
    places: List<Place>,
    onPlaceClick: (Place) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier,
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = places,
            key = {
                it.id
            }
        ) { place ->
            PlaceCard(
                name = "متحف السودان القومي",
                address = "شارع النيل، الخرطوم",
                rating = "4.9",
                tags = listOf("تاريخي", "ثقافة"),
                imageRes = R.drawable.test_image_2
            )
        }
    }
}