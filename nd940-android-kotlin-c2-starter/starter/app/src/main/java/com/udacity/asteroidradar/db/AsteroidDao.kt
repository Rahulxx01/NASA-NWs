package com.udacity.asteroidradar.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.dataGson.NearEarthObjects


@Dao
interface AsteroidDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(asteriod : List<Asteroid>)

    @Query("select * from  current_asteroids")
    fun getAsteroidData(): LiveData<List<Asteroid>>

    @Query("select * from  current_asteroids where id = id" )
    fun getAsteroidSingleData(): LiveData<List<Asteroid>>


    @Query("delete  from  current_asteroids")
    fun deleteOldAsteroidData()


}