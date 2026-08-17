package com.example.kashifapp.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kashifapp.place.domain.model.PlaceCategory

@Composable
fun PlaceChipsList(
    placeCategories: List<PlaceCategory>,
    selectedCategory: PlaceCategory?,
    onCategorySelected: (PlaceCategory) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState()
) {
    LazyRow(
        modifier = modifier,
        state = scrollState,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(
            items = placeCategories,
            key = {
                it.ordinal
            }
        ) {
            PlaceCategoryChip(
                text = it.name,
                isSelected = false
            )
        }
    }
}