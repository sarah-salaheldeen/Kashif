package com.example.kashifapp.place.data.remote

import com.example.kashifapp.place.data.dto.OverpassResponseDto
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface OverpassApiService {
    @POST("interpreter")
    @FormUrlEncoded
    suspend fun queryPlaces(
        @Field("data") query: String
    ): Response<OverpassResponseDto>
}