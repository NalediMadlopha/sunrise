package com.sunrise.app.ui.main

import android.app.Application
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.sunrise.app.model.WeatherMap
import com.sunrise.app.repository.OpenWeatherMapRepositoryContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MainViewModel @Inject constructor(
    application: Application,
    private val geocoder: Geocoder,
    private val openWeatherMapRepository: OpenWeatherMapRepositoryContract,
    private val ioContext: CoroutineContext = Dispatchers.IO
) : AndroidViewModel(application) {

    fun getAddress(locationName: String?): Address? {
        if (!locationName.isNullOrEmpty()) {
            try {
                val addressList = geocoder.getFromLocationName(locationName, 1).orEmpty()
                val address = addressList.firstOrNull()

                if (!addressList.isNullOrEmpty() && (!address?.locality.isNullOrEmpty()
                            || !address?.adminArea.isNullOrEmpty())
                ) {
                    return address
                }
                return null
            } catch (e: IOException) {
                return null
            }
        }
        return null
    }

    fun getAddress(latLng: LatLng?): Address? {
        if (latLng != null) {
            try {
                val addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)

                if (!addressList.isNullOrEmpty()) {
                    return addressList.first()
                }
                return null
            } catch (e: IOException) {
                return null
            }
        }
        return null
    }

    fun fetchWeather(
        latitude: Double,
        longitude: Double,
        units: String = "metric",
        numberOfDays: Int = 7
    ) {
        viewModelScope.launch {
            withContext(ioContext) {
                val response = openWeatherMapRepository.fetchRemoteWeather(latitude, longitude, units, numberOfDays)
                if (response.isSuccessful && response.body() != null && !response.body()?.list.isNullOrEmpty()) {
                    response.body()?.let {
                        val weather = WeatherMap(
                            it.id, it.city, it.cnt, it.cod, it.list, it.message)
                        openWeatherMapRepository.saveWeather(weather)
                    }
                }
            }
        }
    }

    fun getWeather(): LiveData<List<WeatherMap>> {
        return openWeatherMapRepository.getWeather()
    }
}