package com.sunrise.app.di

import android.app.Application
import androidx.room.Room
import com.sunrise.app.database.WeatherMapDao
import com.sunrise.app.database.WeatherMapDatabase
import com.sunrise.app.service.OpenWeatherMapApi
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module(includes = [RepositoryModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideOpenWeatherMapApi(): OpenWeatherMapApi {
        return Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenWeatherMapApi::class.java)
    }

    @Singleton
    @Provides
    fun provideWeatherMapDatabase(app: Application): WeatherMapDatabase {
        return Room
            .databaseBuilder(app, WeatherMapDatabase::class.java, "WeatherMap.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideWeatherMapDao(database: WeatherMapDatabase): WeatherMapDao {
        return database.weatherMapDao()
    }

    @IoDispatcher
    @Provides
    fun providesIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Retention(AnnotationRetention.BINARY)
    @Qualifier
    annotation class IoDispatcher

}