package com.example.kashifapp.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GeminiApiKey

@Qualifier @Retention(AnnotationRetention.BINARY)
annotation class GeoapifyApiKey

@Qualifier @Retention(AnnotationRetention.BINARY)
annotation class GeminiRetrofit     // distinguishes Gemini Retrofit from Geoapify Retrofit