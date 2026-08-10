package com.example.kashifapp.place.data.dto

import com.google.gson.annotations.SerializedName

data class OsmElementDto(
    @SerializedName("type") val type: String,           // "node" or "way"
    @SerializedName("id")   val id: Long,               // raw numeric id
    @SerializedName("lat")  val lat: Double?,           // only on "node"
    @SerializedName("lon")  val lon: Double?,           // only on "node"
    @SerializedName("center") val center: CenterDto?,   // only on "way"
    @SerializedName("tags") val tags: Map<String, String>? = emptyMap()
)

data class CenterDto(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double
)
