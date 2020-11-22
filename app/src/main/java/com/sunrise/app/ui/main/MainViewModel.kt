package com.sunrise.app.ui.main

import android.app.Application
import android.location.Address
import android.location.Geocoder
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.maps.model.LatLng
import java.io.IOException

class MainViewModel @VisibleForTesting constructor(
    application: Application,
    private var geocoder: Geocoder
) : AndroidViewModel(application) {

    constructor(application: Application) : this(application, Geocoder(application))

    fun getAddress(locationName: String?): Address? {
        if (!locationName.isNullOrEmpty()) {
            try {
                val addressList = geocoder.getFromLocationName(locationName, 1).orEmpty()
                val address = addressList.firstOrNull()

                if (!addressList.isNullOrEmpty() && (!address?.locality.isNullOrEmpty()
                            || !address?.adminArea.isNullOrEmpty())) {
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
}