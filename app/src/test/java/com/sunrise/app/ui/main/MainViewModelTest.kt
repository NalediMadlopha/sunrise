package com.sunrise.app.ui.main

import android.app.Application
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.whenever
import com.sunrise.app.repository.OpenWeatherMapRepositoryContract
import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import java.io.IOException

class MainViewModelTest {

    private lateinit var viewModel: MainViewModel

    @Mock
    private lateinit var mockApplication: Application
    @Mock
    private lateinit var mockGeocoder: Geocoder
    @Mock
    private lateinit var mockAddress : Address
    @Mock
    private lateinit var mockRepository: OpenWeatherMapRepositoryContract

    @Before
    fun setUp() {
        initMocks(this)

        viewModel = MainViewModel(mockApplication, mockGeocoder, mockRepository, Dispatchers.Unconfined)
    }

    @Test
    fun `getAddress should return null if location name is null`() {
        assertNull(viewModel.getAddress(locationName = null))
    }

    @Test
    fun `getAddress should return null if location name is an empty string`() {
        assertNull(viewModel.getAddress(""))
    }

    @Test
    fun `getAddress should return null if Goecoder_getLocationFromName throws an IOException`() {
        whenever(mockGeocoder.getFromLocationName(any(), any())).thenThrow(IOException())

        assertNull(viewModel.getAddress("Johannesburg"))
    }

    @Test
    fun `getAddress should return null if Goecoder_getLocationFromName address list is null`() {
        whenever(mockAddress.locality).thenReturn("locality")
        whenever(mockAddress.adminArea).thenReturn("adminArea")
        whenever(mockGeocoder.getFromLocationName(any(), any())).thenReturn(null)

        assertNull(viewModel.getAddress("Johannesburg"))
    }

    @Test
    fun `getAddress should return null if Goecoder_getLocationFromName address list is empty`() {
        whenever(mockAddress.locality).thenReturn("locality")
        whenever(mockAddress.adminArea).thenReturn("adminArea")
        whenever(mockGeocoder.getFromLocationName(any(), any())).thenReturn(emptyList())

        assertNull(viewModel.getAddress("Johannesburg"))
    }

    @Test
    fun `getAddress should return null if getFromLocationName returns address with locality equal to null`() {
        whenever(mockGeocoder.getFromLocationName(any(), any())).thenReturn(listOf(mockAddress))

        assertNull(viewModel.getAddress("Johannesburg"))
    }

    @Test
    fun `getAddress should return null if address locality is an empty string`() {
        whenever(mockAddress.locality).thenReturn("")
        whenever(mockGeocoder.getFromLocationName(any(), any())).thenReturn(listOf(mockAddress))

        assertNull(viewModel.getAddress("Johannesburg"))
    }

    @Test
    fun `getAddress should return the first address if Goecoder_getFromLocationName returns a valid address with valid locality`() {
        whenever(mockAddress.locality).thenReturn("Locality")
        whenever(mockGeocoder.getFromLocationName(any(), any())).thenReturn(listOf(mockAddress))

        assertEquals(mockAddress, viewModel.getAddress("Johannesburg"))
    }

    @Test
    fun `getAddress should return null if getFromLocationName returns address with adminArea equal to null`() {
        whenever(mockAddress.adminArea).thenReturn(null)
        whenever(mockGeocoder.getFromLocationName(any(), any())).thenReturn(listOf(mockAddress))

        assertNull(viewModel.getAddress("Johannesburg"))
    }

    @Test
    fun `getAddress should return null if address adminArea is an empty string`() {
        whenever(mockAddress.adminArea).thenReturn("")
        whenever(mockGeocoder.getFromLocationName(any(), any())).thenReturn(listOf(mockAddress))

        assertNull(viewModel.getAddress("Johannesburg"))
    }

    @Test
    fun `getAddress should return the first address if Goecoder_getFromLocationName returns a valid address with valid adminArea`() {
        whenever(mockAddress.adminArea).thenReturn("adminArea")
        whenever(mockGeocoder.getFromLocationName(any(), any())).thenReturn(listOf(mockAddress))

        assertEquals(mockAddress, viewModel.getAddress("Johannesburg"))
    }

    @Test
    fun `getAddress should return null if latLng is null`() {
        assertNull(viewModel.getAddress(latLng = null))
    }

    @Test
    fun `getAddress should return null if Goecoder_getFromLocation throws an IOException`() {
        whenever(mockGeocoder.getFromLocation(any(), any(), any())).thenThrow(IOException())

        assertNull(viewModel.getAddress(LatLng(28.0, -26.0)))
    }

    @Test
    fun `getAddress should return null if Goecoder_getFromLocation address list is null`() {
        whenever(mockGeocoder.getFromLocation(any(), any(), any())).thenReturn(null)

        assertNull(viewModel.getAddress(LatLng(28.0, -26.0)))
    }

    @Test
    fun `getAddress should return null if Goecoder_getFromLocation address list is empty`() {
        whenever(mockGeocoder.getFromLocation(any(), any(), any())).thenReturn(emptyList())

        assertNull(viewModel.getAddress(LatLng(28.0, -26.0)))
    }

    @Test
    fun `getAddress should return the first address if Goecoder_getFromLocation returns a valid address`() {
        whenever(mockAddress.locality).thenReturn("Locality")
        whenever(mockGeocoder.getFromLocation(any(), any(), any())).thenReturn(listOf(mockAddress))

        assertEquals(mockAddress, viewModel.getAddress(LatLng(28.0, -26.0)))
    }

}
