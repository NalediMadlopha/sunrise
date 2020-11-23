package com.sunrise.app.repository

import androidx.lifecycle.LiveData
import com.sunrise.app.database.WeatherMapDatabase
import com.sunrise.app.model.WeatherMap
import com.sunrise.app.service.OpenWeatherMapApi
import retrofit2.Response
import javax.inject.Inject

class OpenWeatherMapRepository @Inject constructor(
    private val database: WeatherMapDatabase,
    private val service: OpenWeatherMapApi
) : OpenWeatherMapRepositoryContract {

    override fun fetchRemoteWeather(latitude: Double, longitude: Double,
                                    units: String, numberOfDays: Int): Response<WeatherMap> {
        return service.fetchWeather(latitude, longitude, units, numberOfDays).execute()
    }

    override suspend fun getWeather(id: Int): LiveData<WeatherMap> {
        return database.weatherMapDao().findById(id)
    }

    override suspend fun getWeather(): LiveData<List<WeatherMap>> {
        return database.weatherMapDao().getAll()
    }

}