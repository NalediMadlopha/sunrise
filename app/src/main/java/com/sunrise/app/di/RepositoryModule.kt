package com.sunrise.app.di

import com.sunrise.app.database.WeatherMapDatabase
import com.sunrise.app.repository.OpenWeatherMapRepository
import com.sunrise.app.service.OpenWeatherMapApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideOpenWeatherMapRepository(
        database: WeatherMapDatabase, service: OpenWeatherMapApi
    ): OpenWeatherMapRepository {
        return OpenWeatherMapRepository(database, service)
    }

}