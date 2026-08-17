package com.example.kashifapp.place.data.dto

import com.google.gson.annotations.SerializedName

data class GeoapifyResponseDto(
    @SerializedName("features") val features: List<GeoapifyFeatureDto> = emptyList()
)

data class GeoapifyFeatureDto(
    @SerializedName("properties") val properties: GeoapifyPropertiesDto,
    @SerializedName("geometry")   val geometry: GeoapifyGeometryDto
)

data class GeoapifyPropertiesDto(
    @SerializedName("place_id")     val placeId: String,
    @SerializedName("name")         val name: String?,
    @SerializedName("name_international") val nameInternational: Map<String, String>?,
    @SerializedName("categories")   val categories: List<String> = emptyList(),
    @SerializedName("address_line1") val addressLine1: String?,
    @SerializedName("address_line2") val addressLine2: String?,
    @SerializedName("city")         val city: String?,
    @SerializedName("phone")        val phone: String?,
    @SerializedName("website")      val website: String?,
    @SerializedName("opening_hours") val openingHours: String?,
    @SerializedName("facilities")   val facilities: Map<String, Any>? = null,
    @SerializedName("catering")     val catering: Map<String, Any>? = null
)

data class GeoapifyGeometryDto(
    @SerializedName("coordinates") val coordinates: List<Double>  // [lon, lat]
)