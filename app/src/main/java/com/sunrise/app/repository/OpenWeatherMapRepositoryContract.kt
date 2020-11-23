package com.sunrise.app.repository

import androidx.lifecycle.LiveData
import com.sunrise.app.model.WeatherMap
import retrofit2.Response

interface OpenWeatherMapRepositoryContract {

    fun fetchRemoteWeather(
        latitude: Double,
        longitude: Double,
        units: String,
        numberOfDays: Int
    ): Response<WeatherMap>

    suspend fun getWeather(id: Int): LiveData<WeatherMap>

    suspend fun getWeather(): LiveData<List<WeatherMap>>

}