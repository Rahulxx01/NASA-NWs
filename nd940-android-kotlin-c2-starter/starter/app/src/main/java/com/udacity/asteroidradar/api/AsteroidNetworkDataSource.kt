package com.udacity.asteroidradar.api

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.model.CurrentAsteroidResponse

interface AsteroidNetworkDataSource {
    val downloadedCurrentAsteroids : LiveData<CurrentAsteroidResponse>

    suspend fun  fetechCurrentAsteroids(
        start_date : String,
        end_date : String
    )
}