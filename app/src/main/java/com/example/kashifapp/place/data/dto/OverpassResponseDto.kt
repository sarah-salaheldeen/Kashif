package com.example.kashifapp.place.data.dto

import com.google.gson.annotations.SerializedName

data class OverpassResponseDto(
    @SerializedName("elements") val elements: List<OsmElementDto> = emptyList()
)
