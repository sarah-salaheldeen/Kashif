package com.example.kashifapp.place.data.mapper

import com.example.kashifapp.place.data.dto.OsmElementDto
import com.example.kashifapp.place.data.local.PlaceEntity
import com.example.kashifapp.place.domain.model.Place
import com.example.kashifapp.place.domain.model.PlaceCategory
import com.example.kashifapp.place.domain.model.PlaceDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// ─── OSM tag keys that get mapped to named PlaceDetails fields.
// Everything else lands in extraTags for Mood Search keyword matching.
private val MAPPED_TAG_KEYS = setOf(
    "name", "name:en", "name:ar",
    "amenity", "leisure", "tourism", "shop",
    "addr:street", "addr:city",
    "phone", "contact:phone",
    "website", "contact:website",
    "opening_hours", "cuisine", "description", "capacity"
)

// ─── OsmElementDto → Place (used after fetching from Overpass)
fun OsmElementDto.toPlace(cityId: String): Place? {
    val placeLat = lat ?: center?.lat ?: return null
    val placeLong = lon ?: center?.lon ?: return null
    val placeTags = tags ?: return null
    val nameEn = tags["name:en"] ?: tags["name"] ?: return null

    return Place(
        id = "$type/$id",
        nameEn = nameEn,
        nameAr = placeTags["name:ar"],
        category = categoryFromOsmTags(placeTags),
        placeDetails = PlaceDetails(
            address = buildAddress(placeTags),
            phone = placeTags["phone"] ?: placeTags["contact:phone"],
            webSite = placeTags["website"] ?: placeTags["contact:website"],
            openingHours = placeTags["opening_hours"],
            cuisine = placeTags["cuisine"],
            description = placeTags["description"],
            capacity = placeTags["capacity"]?.toIntOrNull(),
            extraInfo = placeTags.filterKeys { it !in MAPPED_TAG_KEYS }
        ),
        lat = placeLat,
        lon = placeLong,
        lastSyncedAt = System.currentTimeMillis(),
        name = tags["name"] ?: nameEn,
        city = cityId,
        isSaved = false
    )
}

// ─── Place → PlaceEntity (for Room insertion)
fun Place.toPlaceEntity(): PlaceEntity = PlaceEntity(
    id = id,
    nameEn = nameEn,
    nameAr = nameAr ?: name,
    category = category.name,
    address = placeDetails?.address,
    phone = placeDetails?.phone,
    website = placeDetails?.webSite,
    openingHours = placeDetails?.openingHours,
    cuisine = placeDetails?.cuisine,
    description = placeDetails?.description,
    capacity = placeDetails?.capacity,
    extraTagsJson = Gson().toJson(placeDetails?.extraInfo),
    lat = lat,
    long = lon,
    city = city,
    isSaved = isSaved,
    lastSyncedAt = lastSyncedAt
)

// ─── PlaceEntity → Place (for reading from Room)
fun PlaceEntity.toPlace(): Place = Place(
    id = id,
    nameEn = nameEn,
    nameAr = nameAr,
    name = nameAr,
    category = PlaceCategory.valueOf(category),
    placeDetails = PlaceDetails(
        address = address,
        phone = phone,
        webSite = website,
        openingHours = openingHours,
        cuisine = cuisine,
        description = description,
        capacity = capacity,
        extraInfo = Gson().fromJson(
            extraTagsJson,
            object : TypeToken<Map<String, String>>() {}.type
        ) ?: emptyMap()
    ),
    lat = lat,
    lon = long,
    city = city,
    isSaved = isSaved,
    lastSyncedAt = lastSyncedAt
)


// ─── Internal helpers — OSM knowledge stays in this file only

private fun categoryFromOsmTags(tags: Map<String, String>): PlaceCategory = when {
    tags["amenity"] == "restaurant" -> PlaceCategory.RESTAURANT
    tags["amenity"] == "cafe" -> PlaceCategory.CAFE
    tags["amenity"] == "place_of_worship" -> PlaceCategory.MOSQUE
    tags["amenity"] == "pharmacy" -> PlaceCategory.PHARMACY
    tags["leisure"] == "park" -> PlaceCategory.PARK
    tags["leisure"] == "fitness_centre" -> PlaceCategory.GYM
    tags["tourism"] == "museum" -> PlaceCategory.MUSEUM
    tags["tourism"] == "hotel" -> PlaceCategory.HOTEL
    tags["shop"] == "mall" || tags["shop"] == "supermarket" -> PlaceCategory.SHOPPING
    tags["shop"] == "bakery" -> PlaceCategory.BAKERY
    else -> PlaceCategory.OTHER
}

private fun buildAddress(tags: Map<String, String>): String? =
    listOfNotNull(tags["addr:street"], tags["addr:city"])
        .joinToString(", ")
        .ifBlank { null }

// OSM key/value for each category — used by the data source to build queries
internal fun PlaceCategory.toOsmFilter(): String {
    val (key, value ) = when(this) {
        PlaceCategory.RESTAURANT -> "amenity" to "restaurant"
        PlaceCategory.CAFE -> "amenity" to "cafe"
        PlaceCategory.PARK -> "leisure" to "park"
        PlaceCategory.MUSEUM     -> "tourism" to "museum"
        PlaceCategory.SHOPPING   -> "shop"    to "mall"
        PlaceCategory.MOSQUE     -> "amenity" to "place_of_worship"
        PlaceCategory.HOTEL      -> "tourism" to "hotel"
        PlaceCategory.GYM        -> "leisure" to "fitness_centre"
        PlaceCategory.BAKERY     -> "shop"    to "bakery"
        PlaceCategory.PHARMACY   -> "amenity" to "pharmacy"
        else -> return ""
    }
    return """"$key"="$value""""
}