package com.example.kashifapp.place.presentation.placeslist.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.kashifapp.R
import com.example.kashifapp.ui.theme.ChipBackgroundSelected
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect

@Composable
fun FavoriteButton(
    isFavorited: Boolean,
    hazeState: HazeState,
    modifier: Modifier = Modifier
) {
    val heartColor = if (isFavorited) ChipBackgroundSelected else Color.White

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(40.dp)
            .hazeEffect(
                state = hazeState,
                style = HazeStyle(
                    backgroundColor = Color.White.copy(alpha = 0.8f),
                    blurRadius = 4.dp,
                    tints = listOf(HazeTint(Color.White.copy(alpha = 0.15f)))
                )
            )
    ) {
        // Add an icon or image here if needed, but the original code had an empty Box scope
        Image(
            painter = painterResource(id = if (isFavorited) R.drawable.baseline_favorite_24 else R.drawable.outline_favorite_border_24),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
    }
}
