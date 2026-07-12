package com.example.kashifapp.ai

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kashifapp.R
import com.example.kashifapp.ui.theme.*
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource

@Composable
fun AiPlaceCard(
    name: String,
    address: String,
    rating: String,
    tags: List<String>,
    modifier: Modifier = Modifier,
    imageRes: Int = R.drawable.ic_launcher_background,
    onFavoriteClick: () -> Unit = {}
) {
    val hazeState = remember { HazeState() }
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .hazeSource(hazeState),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .size(44.dp)
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
                        Icon(
                            painter = painterResource(id = R.drawable.ic_favorite),
                            contentDescription = "Favorite",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = name,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = ColorPrimaryText
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = address,
                                fontSize = 14.sp,
                                color = LocationTextColor
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = rating,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = ColorSecondaryText
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_star),
                                contentDescription = null,
                                tint = StarColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        tags.forEach { tag ->
                            TagChip(text = tag)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TagChip(text: String) {
    Surface(
        color = TagBackground,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(start = 8.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            color = ColorSecondaryText,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AiPlaceCardPreview() {
    KashifAppTheme {
        Box(modifier = Modifier.background(BackgroundColor).padding(16.dp)) {
            AiPlaceCard(
                name = "متحف السودان القومي",
                address = "شارع النيل، الخرطوم",
                rating = "4.9",
                tags = listOf("تاريخي", "ثقافة"),
                imageRes = R.drawable.test_image_2
            )
        }
    }
}
