package com.example.kashifapp.core.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kashifapp.R
import com.example.kashifapp.ui.theme.ColorSecondaryText
import com.example.kashifapp.ui.theme.DarkBrown
import com.example.kashifapp.ui.theme.KashifAppTheme
import com.example.kashifapp.ui.theme.PillBackground
import com.example.kashifapp.ui.theme.ProfileBorder
import com.example.kashifapp.ui.theme.TopBarBackground


@Composable
fun TopBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(TopBarBackground)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.clickable { /*onProfileClick()*/ }
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .border(width = 2.dp, color = ProfileBorder, shape = CircleShape)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "Profile image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBrown
            )
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .background(PillBackground)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_location_pin),
                contentDescription = stringResource(R.string.location_icon_content_description),
                modifier = Modifier
            )
            Text(
                text = stringResource(R.string.khartoum),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = ColorSecondaryText
            )
            Image(
                painter = painterResource(R.drawable.ic_down_arrow),
                contentDescription = stringResource(R.string.location_selector_content_description),
                modifier = Modifier
                    .size(12.dp)
            )
        }
    }
}

@Preview(locale = "ar")
@Composable
fun TopBarPreview() {
    KashifAppTheme {
        TopBar()
    }
}