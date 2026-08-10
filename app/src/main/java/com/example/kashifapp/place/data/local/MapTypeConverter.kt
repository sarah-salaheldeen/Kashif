package com.example.kashifapp.place.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MapTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromMap(map: Map<String, String>): String = gson.toJson(map)

    @TypeConverter
    fun toMap(json: String): Map<String, String> =
        gson.fromJson(json, object : TypeToken<Map<String, String>> () {}.type)
            ?: emptyMap()
}