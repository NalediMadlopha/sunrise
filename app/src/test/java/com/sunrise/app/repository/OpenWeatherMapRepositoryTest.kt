package com.sunrise.app.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.sunrise.app.database.WeatherMapDao
import com.sunrise.app.database.WeatherMapDatabase
import com.sunrise.app.model.City
import com.sunrise.app.model.Coord
import com.sunrise.app.model.WeatherMap
import com.sunrise.app.service.OpenWeatherMapApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations.initMocks
import retrofit2.Call
import retrofit2.Response

class OpenWeatherMapRepositoryTest {

    private lateinit var repository: OpenWeatherMapRepository
    private lateinit var mockWebServer: MockWebServer

    @Mock
    private lateinit var mockDatabase: WeatherMapDatabase
    @Mock
    private lateinit var mockWeatherMapDao: WeatherMapDao
    @Mock
    private lateinit var mockService: OpenWeatherMapApi
    @Mock
    private lateinit var mockWeatherMapResponseCall: Call<WeatherMap>

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        initMocks(this)
        mockWebServer = MockWebServer()

        repository = OpenWeatherMapRepository(mockDatabase, mockService)
    }

    @Test
    fun `fetchRemoteWeather should invoke the fetchWeather service call`() {
        val successResponse = Response.success(getWeatherMap())

        `when`(mockService.fetchWeather(28.63, -26.36, "metric", 7)).thenReturn(mockWeatherMapResponseCall)
        `when`(mockWeatherMapResponseCall.execute()).thenReturn(successResponse)

        repository.fetchRemoteWeather(28.63, -26.36, "metric", 7)

        verify(mockService).fetchWeather(28.63, -26.36, "metric", 7)
    }

    @Test
    fun `getWeather with id parameter should invoke WeatherMapDao findById`() {
        `when`(mockDatabase.weatherMapDao()).thenReturn(mockWeatherMapDao)

        runBlocking { repository.getWeather(1) }

        verify(mockWeatherMapDao).findById(1)
    }

    @Test
    fun `getWeather with id parameter should return the same weather object returned from the database`() {
        val expectedWeatherMap = MutableLiveData<WeatherMap>()
        expectedWeatherMap.value = getWeatherMap()

        `when`(mockDatabase.weatherMapDao()).thenReturn(mockWeatherMapDao)
        `when`(mockWeatherMapDao.findById(1)).thenReturn(expectedWeatherMap)

        runBlocking {
            assertEquals(expectedWeatherMap, repository.getWeather(1))
        }
    }

    @Test
    fun `getWeather should invoke WeatherMapDao getAll`() {
        `when`(mockDatabase.weatherMapDao()).thenReturn(mockWeatherMapDao)

        runBlocking { repository.getWeather() }

        verify(mockWeatherMapDao).getAll()
    }

    @Test
    fun `getWeather should return the same weather object returned from the database`() {
        val expectedWeatherMap = MutableLiveData<List<WeatherMap>>()
        expectedWeatherMap.value = listOf(getWeatherMap())

        `when`(mockDatabase.weatherMapDao()).thenReturn(mockWeatherMapDao)
        `when`(mockWeatherMapDao.getAll()).thenReturn(expectedWeatherMap)

        runBlocking {
            assertEquals(expectedWeatherMap, repository.getWeather())
        }
    }

    private fun getWeatherMap() : WeatherMap {
        val coord = Coord(28.43, -26.32)
        val city = City(coord, "ZA", 1, "Johannesburg", 2026469, 7400)
        return WeatherMap(1,
            city, 7, "200", listOf(), 0.0322609)
    }


}