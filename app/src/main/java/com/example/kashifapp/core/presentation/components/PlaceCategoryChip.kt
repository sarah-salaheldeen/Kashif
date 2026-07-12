package com.example.kashifapp.core.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kashifapp.R
import com.example.kashifapp.ui.theme.ChipBackgroundSelected
import com.example.kashifapp.ui.theme.ChipBackgroundUnselected
import com.example.kashifapp.ui.theme.KashifAppTheme

@Composable
fun PlaceCategoryChip(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .widthIn(min = 70.dp),
        shape = RoundedCornerShape(percent = 50),
        color = if (isSelected) ChipBackgroundSelected else ChipBackgroundUnselected,
        contentColor = if (isSelected) Color.White else Color(0xFF6C6150)
    ) {
        Text(
            text = stringResource(R.string.all),
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 24.dp),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFCF6F4, locale = "ar")
@Composable
fun SearchBarPreview() {
    KashifAppTheme {
        PlaceCategoryChip(
            text = stringResource(R.string.all),
            isSelected = true
        )
    }
}