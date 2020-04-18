package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Asteroid

interface AsteroidRepository {

    suspend fun getAsteroidData() : LiveData<out List<Asteroid>>

    //suspend fun getAsteroidData() : LiveData<out List<Asteroid>>
}