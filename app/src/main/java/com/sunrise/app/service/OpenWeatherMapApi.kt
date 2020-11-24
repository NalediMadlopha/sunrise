package com.sunrise.app.service

import com.sunrise.app.BuildConfig
import com.sunrise.app.model.WeatherMap
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherMapApi {

    @GET("/data/2.5/forecast/daily")
    fun fetchWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("units") units: String,
        @Query("cnt") numberOfDays: Int,
        @Query("appid") appId: String = BuildConfig.OPEN_WEATHER_MAP_API_KEY
    ): Call<WeatherMap>

}