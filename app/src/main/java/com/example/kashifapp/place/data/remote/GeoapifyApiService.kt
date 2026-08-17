package com.example.kashifapp.place.data.remote

import com.example.kashifapp.place.data.dto.GeoapifyResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoapifyApiService {

    @GET("v2/places")
    suspend fun getPlaces(
        @Query("categories") categories: String, // "catering.cafe,catering.restaurant"
        @Query("filter") filter: String, // "circle:lon,lat,radius"
        @Query("bias") bias: String, // "proximity:lon,lat"
        @Query("limit") limit: Int = 50,
        @Query("offset") offset: Int = 0,
        @Query("lang") language: String = "ar", // Arabic names where available
        @Query("apiKey") apiKey: String
    ): Response<GeoapifyResponseDto>
}