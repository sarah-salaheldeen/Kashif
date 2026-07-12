package com.example.kashifapp.ai

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kashifapp.navigation.TopLevelDestination
import com.example.kashifapp.navigation.AppDestinations
import com.example.kashifapp.ui.theme.ChipBackgroundSelected
import com.example.kashifapp.ui.theme.ColorSecondaryText
import com.example.kashifapp.ui.theme.Grey
import com.example.kashifapp.ui.theme.KashifAppTheme

@Composable
fun KashifBottomNav(
    destinations: List<TopLevelDestination>,
    isSelected: (TopLevelDestination) -> Boolean,
    onDestinationClick: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        color = Grey,
        modifier = modifier
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            contentColor = ColorSecondaryText,
            tonalElevation = 0.dp,
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
        ) {
            destinations.forEach { destination ->
                val selected = isSelected(destination)

                NavigationBarItem(
                    selected = selected,
                    onClick = { onDestinationClick(destination) },
                    icon = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = if (selected) Modifier.padding(horizontal = 12.dp) else Modifier
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (selected) destination.iconSelectedRes
                                        else destination.iconRes
                                ),
                                contentDescription = stringResource(destination.labelRes),
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = stringResource(destination.labelRes),
                                maxLines = 1,
                                fontSize = 12.sp
                            )
                        }
                    },
                    label = null,
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = Color.White,
                        indicatorColor = ChipBackgroundSelected,
                        unselectedIconColor = ColorSecondaryText,
                        unselectedTextColor = ColorSecondaryText
                    )
                )
            }
        }
    }
}

@Preview(locale = "ar")
@Composable
fun KashifBottomNavPreview() {
    KashifAppTheme {
        KashifBottomNav(
            destinations = AppDestinations.topLevelDestinations,
            isSelected = { it == AppDestinations.topLevelDestinations[0] },
            onDestinationClick = {}
        )
    }
}

@Preview(uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES, locale = "ar")
@Composable
fun KashifBottomNavDarkPreview() {
    KashifAppTheme {
        KashifBottomNav(
            destinations = AppDestinations.topLevelDestinations,
            isSelected = { it == AppDestinations.topLevelDestinations[0] },
            onDestinationClick = {}
        )
    }
}
