package com.example.kashifapp.place.presentation.util

import androidx.compose.ui.graphics.Color
import com.example.kashifapp.R
import com.example.kashifapp.place.domain.model.PlaceCategory

fun PlaceCategory.placeholderColor(): Color = when (this) {
    PlaceCategory.RESTAURANT -> Color(0xFF8F341B)
    PlaceCategory.CAFE -> Color(0xFF6D4C41)
    PlaceCategory.PARK -> Color(0xFF388E3C)
    PlaceCategory.MUSEUM -> Color(0xFF1565C0)
    PlaceCategory.SHOPPING -> Color(0xFF6A1B9A)
    PlaceCategory.MOSQUE -> Color(0xFF00695C)
    PlaceCategory.HOTEL -> Color(0xFFF57F17)
    PlaceCategory.GYM -> Color(0xFFAD1457)
    PlaceCategory.BAKERY -> Color(0xFFE65100)
    PlaceCategory.PHARMACY -> Color(0xFF2E7D32)
    PlaceCategory.OTHER -> Color(0xFF8F341B)
}

fun PlaceCategory.iconRes(): Int = when (this) {
    PlaceCategory.RESTAURANT -> R.drawable.ic_restaurant
    PlaceCategory.CAFE -> R.drawable.ic_cafe
    PlaceCategory.PARK -> R.drawable.ic_park
    PlaceCategory.MUSEUM -> R.drawable.ic_museum
    PlaceCategory.SHOPPING -> R.drawable.ic_shopping
    PlaceCategory.MOSQUE -> R.drawable.ic_mosque
    PlaceCategory.HOTEL -> R.drawable.ic_hotel
    PlaceCategory.GYM -> R.drawable.ic_gym
    PlaceCategory.BAKERY -> R.drawable.ic_bakery
    PlaceCategory.PHARMACY -> R.drawable.ic_pharmacy
    PlaceCategory.OTHER -> R.drawable.ic_shopping
}

fun PlaceCategory.displayName(isArabic: Boolean): String = when (this) {
    PlaceCategory.RESTAURANT -> if (isArabic) "مطعم" else "Restaurant"
    PlaceCategory.CAFE -> if (isArabic) "مقهى" else "Café"
    PlaceCategory.PARK -> if (isArabic) "حديقة" else "Park"
    PlaceCategory.MUSEUM -> if (isArabic) "متحف" else "Museum"
    PlaceCategory.SHOPPING -> if (isArabic) "تسوق" else "Shopping"
    PlaceCategory.MOSQUE -> if (isArabic) "مسجد" else "Mosque"
    PlaceCategory.HOTEL -> if (isArabic) "فندق" else "Hotel"
    PlaceCategory.GYM -> if (isArabic) "صالة رياضية" else "Gym"
    PlaceCategory.BAKERY -> if (isArabic) "مخبز" else "Bakery"
    PlaceCategory.PHARMACY -> if (isArabic) "صيدلية" else "Pharmacy"
    PlaceCategory.OTHER -> if (isArabic) "أخرى" else "Other"
}