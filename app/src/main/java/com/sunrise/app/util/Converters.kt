package com.sunrise.app.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sunrise.app.model.City
import com.sunrise.app.model.Forecast
import java.lang.reflect.Type


class Converters {

    @TypeConverter
    fun forecastListFromString(value: String?): List<Forecast>? {
        val listType: Type = object : TypeToken<List<Forecast?>?>() {}.type
        return Gson().fromJson<List<Forecast>>(value, listType)
    }

    @TypeConverter
    fun forecastListToString(list: List<Forecast?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun cityFromString(value: String?): City? {
        val listType: Type = object : TypeToken<City?>() {}.type
        return Gson().fromJson<City>(value, listType)
    }

    @TypeConverter
    fun cityToString(list: City?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}