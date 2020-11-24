package com.sunrise.app.repository

import androidx.lifecycle.LiveData
import com.sunrise.app.model.WeatherMap
import retrofit2.Response

interface OpenWeatherMapRepositoryContract {

    fun fetchRemoteWeather(
        latitude: Double,
        longitude: Double,
        units: String = "metrics",
        numberOfDays: Int = 7
    ): Response<WeatherMap>

    suspend fun saveWeather(weather: WeatherMap)

    suspend fun getWeather(id: Int): LiveData<WeatherMap>

    fun getWeather(): LiveData<List<WeatherMap>>

}