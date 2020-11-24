package com.sunrise.app.service

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.any
import com.sunrise.app.BuildConfig
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OpenWeatherMapApiTest {

    private lateinit var service: OpenWeatherMapApi
    private lateinit var mockWebServer: MockWebServer

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenWeatherMapApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `fetchWeather should hit the daily endpoint`() {
        val expectedEndPoint =
            "/data/2.5/forecast/daily?lat=28.63&lon=-26.36&units=metric&cnt=7&appid=${BuildConfig.OPEN_WEATHER_MAP_API_KEY}"
        enqueueResponse("error.json")

        service.fetchWeather(28.63, -26.36, "metric", 7).execute()

        assertEquals(expectedEndPoint, mockWebServer.takeRequest().path)
    }

    @Test
    fun `fetchWeather should return a null forecast list if the service returns an error response`() {
        enqueueResponse("error.json")

        val response = service.fetchWeather(any(), any(), any(), any()).execute()
        mockWebServer.takeRequest()

        assertTrue(response.body()?.list.isNullOrEmpty())
    }

    @Test
    fun `fetchWeather should return a list of daily forecast if the response is successful`() {
        enqueueResponse("forecast.json")

        val response = service.fetchWeather(28.63, -26.36, "metric", 7).execute()
        mockWebServer.takeRequest()

        assertTrue(response.isSuccessful)
        assertFalse(response.body()?.list.isNullOrEmpty())
    }

    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        javaClass.classLoader?.let {
            val inputStream = it.getResourceAsStream("api-response/$fileName")
            val source = inputStream.source().buffer()
            val mockResponse = MockResponse()

            for ((key, value) in headers) {
                mockResponse.addHeader(key, value)
            }

            mockWebServer.enqueue(mockResponse.setBody(source.readString(Charsets.UTF_8)))
        }
    }
}