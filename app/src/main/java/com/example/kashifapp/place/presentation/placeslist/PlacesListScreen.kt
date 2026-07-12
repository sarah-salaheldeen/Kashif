package com.example.kashifapp.place.presentation.placeslist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kashifapp.R
import com.example.kashifapp.core.presentation.components.PlaceChipsList
import com.example.kashifapp.core.presentation.components.TopBar
import com.example.kashifapp.place.domain.Place
import com.example.kashifapp.place.domain.PlaceCategory
import com.example.kashifapp.place.presentation.placeslist.components.PlaceList
import com.example.kashifapp.place.presentation.placeslist.components.SearchBar
import com.example.kashifapp.ui.theme.BackgroundColor
import com.example.kashifapp.ui.theme.ChipBackgroundSelected
import com.example.kashifapp.ui.theme.ColorPrimaryText
import com.example.kashifapp.ui.theme.KashifAppTheme
import com.mapbox.maps.extension.style.expressions.dsl.generated.color
import com.mapbox.maps.extension.style.expressions.dsl.generated.mod

@Composable
fun PlacesListScreen(
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
        SearchBar()
        PlaceChipsList(
            placeCategories = listOf(PlaceCategory.RESTAURANT, PlaceCategory.CAFE, PlaceCategory.PARK, PlaceCategory.MUSEUM, PlaceCategory.SHOPPING, PlaceCategory.MOSQUE, PlaceCategory.HOTEL, PlaceCategory.GYM, PlaceCategory.BAKERY, PlaceCategory.PHARMACY),
            modifier = Modifier
                .padding(bottom = 24.dp)
        )
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
        PlaceList(
            listOf(
                Place(
                    id = "1",
                    lat = 30.0444,
                    lon = 31.2357,
                    nameAr = "متحف السودان القومي",
                    nameEn = "Sudanese National Museum",
                    name = "متحف السودان القومي",
                    placeDetails = null,
                    category = PlaceCategory.MUSEUM
                ),
                Place(
                    id = "2",
                    lat = 30.0444,
                    lon = 31.2357,
                    nameAr = "متحف السودان القومي",
                    nameEn = "Sudanese National Museum",
                    name = "متحف السودان القومي",
                    placeDetails = null,
                    category = PlaceCategory.MUSEUM
                ),
                Place(
                    id = "3",
                    lat = 30.0444,
                    lon = 31.2357,
                    nameAr = "متحف السودان القومي",
                    nameEn = "Sudanese National Museum",
                    name = "متحف السودان القومي",
                    placeDetails = null,
                    category = PlaceCategory.MUSEUM
                ),
                Place(
                    id = "4",
                    lat = 30.0444,
                    lon = 31.2357,
                    nameAr = "متحف السودان القومي",
                    nameEn = "Sudanese National Museum",
                    name = "متحف السودان القومي",
                    placeDetails = null,
                    category = PlaceCategory.MUSEUM
                ),
                Place(
                    id = "5",
                    lat = 30.0444,
                    lon = 31.2357,
                    nameAr = "متحف السودان القومي",
                    nameEn = "Sudanese National Museum",
                    name = "متحف السودان القومي",
                    placeDetails = null,
                    category = PlaceCategory.MUSEUM
                )
            ),
            onPlaceClick = {}
        )
    }
}

@Preview(locale = "ar")
@Composable
fun PlacesListScreenPreview() {
    KashifAppTheme {
        PlacesListScreen()
    }
}