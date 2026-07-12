package com.example.kashifapp.ai

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kashifapp.ui.theme.ChipBackgroundSelected
import com.example.kashifapp.ui.theme.ChipBackgroundUnselected
import com.example.kashifapp.ui.theme.ColorSecondaryText
import com.example.kashifapp.ui.theme.KashifAppTheme

@Composable
fun CategoryChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(percent = 50),
        color = if (isSelected) ChipBackgroundSelected else ChipBackgroundUnselected,
        contentColor = if (isSelected) Color.White else ColorSecondaryText
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .widthIn(min = 64.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryChipPreview() {
    KashifAppTheme {
        CategoryChip(
            text = "الكل",
            isSelected = true,
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryChipUnselectedPreview() {
    KashifAppTheme {
        CategoryChip(
            text = "متاجر",
            isSelected = false,
            onClick = {}
        )
    }
}
