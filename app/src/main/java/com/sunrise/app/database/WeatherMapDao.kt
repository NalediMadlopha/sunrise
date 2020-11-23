package com.sunrise.app.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sunrise.app.model.WeatherMap

@Dao
abstract class WeatherMapDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(weatherMap: WeatherMap)

    @Query("SELECT * FROM weather_map")
    abstract fun getAll(): LiveData<List<WeatherMap>>

    @Query("SELECT * FROM weather_map WHERE id=:id")
    abstract fun findById(id: String): LiveData<WeatherMap>

}
