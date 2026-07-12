package com.example.kashifapp

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.LocaleListCompat
import com.example.kashifapp.navigation.AppNavigation
import com.example.kashifapp.place.presentation.placeslist.PlacesListScreen
import com.example.kashifapp.ui.theme.KashifAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Force the app to use Arabic locale temporary
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("ar")
        AppCompatDelegate.setApplicationLocales(appLocale)

        // 1. Force Light Theme regardless of system settings
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                    android.graphics.Color.TRANSPARENT,
                    android.graphics.Color.TRANSPARENT
                )
        )

        setContent {
            KashifAppTheme(darkTheme = false) {
                AppNavigation()
            }
        }
    }
}

@Preview(showBackground = true, locale = "ar")
@Composable
fun GreetingPreview() {
    KashifAppTheme {
        AppNavigation()
    }
}