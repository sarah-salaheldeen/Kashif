package com.example.kashifapp.place.presentation.placeslist.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kashifapp.R
import com.example.kashifapp.ai.TagChip
import com.example.kashifapp.place.domain.model.Place
import com.example.kashifapp.place.domain.model.displayName
import com.example.kashifapp.place.presentation.util.iconRes
import com.example.kashifapp.place.presentation.util.placeholderColor
import com.example.kashifapp.ui.theme.BackgroundColor
import com.example.kashifapp.ui.theme.Brown
import com.example.kashifapp.ui.theme.ColorPrimaryText
import com.example.kashifapp.ui.theme.ColorSecondaryText
import com.example.kashifapp.ui.theme.Grey
import com.example.kashifapp.ui.theme.KashifAppTheme
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource

@Composable
fun PlaceCard(
    place: Place,
    modifier: Modifier = Modifier,
    imageRes: Int = R.drawable.ic_launcher_background,
    onFavoriteClick: () -> Unit = {},
    onPlaceClick: () -> Unit
) {
    val isArabic = LocalLayoutDirection.current == LayoutDirection.Rtl
    val hazeState = remember { HazeState() }
    Card(
        onClick = onPlaceClick,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(contentColor = Color.White, containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                // OSM has no images — placeholder with category color for now.
                // In a future iteration this could be enriched via
                // Google Places Photos API or Wikimedia Commons.
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(place.category.placeholderColor())
                        .hazeSource(state = hazeState),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(place.category.iconRes()),
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.4f),
                        modifier = Modifier.size(64.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .size(40.dp)
                        .align(Alignment.TopEnd)
                        .shadow(
                            elevation = 2.dp,
                            shape = CircleShape,
                            ambientColor = Color.Black.copy(alpha = 0.5f),
                            spotColor = Color.Black.copy(alpha = 0.5f)
                        )
                        .clip(CircleShape)
                        .hazeEffect(
                            state = hazeState,
                            style = HazeStyle(
                                blurRadius = 4.dp,
                                tints = listOf(HazeTint(Color.White.copy(alpha = 0.8f)))
                            )
                        )
                        .clickable { onFavoriteClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_favorite),
                        contentDescription = "Favorite",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = place.displayName(isArabic),
                        color = ColorPrimaryText,
                        fontSize = 20.sp
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_star),
                            contentDescription = null,
                        )
                        Text(
                            text = "5.0"/*place.rating*/,
                            color = Brown,
                            fontSize = 14.sp
                        )
                    }
                }
                place.placeDetails?.address?.let {
                    Text(
                        text = it,
                        color = ColorSecondaryText,
                        fontSize = 16.sp
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    TagChip(text = place.category.name)
                    place.placeDetails?.cuisine?.let { cuisine ->
                        TagChip(text = cuisine)
                    }
                }
            }
        }
    }
}

@Composable
fun TagChip(text: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Grey,
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Text(
            text = text,
            color = ColorSecondaryText,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(vertical = 4.dp, horizontal = 12.dp)
        )
    }
}

@Preview(showBackground = true, locale = "ar")
@Composable
fun PlaceCardPreview() {
    KashifAppTheme {
        Box(modifier = Modifier.background(BackgroundColor).padding(16.dp)) {
            /*PlaceCard(

            )*/
        }
    }
}