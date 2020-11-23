package com.sunrise.app.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sunrise.app.model.WeatherMap
import com.sunrise.app.util.Converters

@Database(entities = [WeatherMap::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class WeatherMapDatabase : RoomDatabase() {

    abstract fun weatherMapDao(): WeatherMapDao

}