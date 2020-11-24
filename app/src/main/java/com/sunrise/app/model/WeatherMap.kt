package com.sunrise.app.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_map")
data class WeatherMap(
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "city")
    val city: City,
    @ColumnInfo(name = "cnt")
    val cnt: Int,
    @ColumnInfo(name = "cod")
    val cod: String,
    @ColumnInfo(name = "list")
    val list: List<Forecast>,
    @ColumnInfo(name = "message")
    val message: Double
)

data class City(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val timezone: Int
)

data class Forecast(
    val clouds: Int,
    val deg: Int,
    val dt: Long,
    val feels_like: FeelsLike,
    val humidity: Int,
    val pop: Double,
    val pressure: Int,
    val rain: Double,
    val speed: Double,
    val sunrise: Int,
    val sunset: Int,
    val temp: Temp,
    val weather: List<Weather>
)

data class Coord(
    val lat: Double,
    val lon: Double
)

data class FeelsLike(
    val day: Double,
    val eve: Double,
    val morn: Double,
    val night: Double
)

data class Temp(
    val day: Double,
    val eve: Double,
    val max: Double,
    val min: Double,
    val morn: Double,
    val night: Double
)

data class Weather(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)