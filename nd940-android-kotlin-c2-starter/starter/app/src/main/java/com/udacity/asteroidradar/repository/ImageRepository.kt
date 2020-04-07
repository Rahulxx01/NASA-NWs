package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.dataImage.ImageModel

interface ImageRepository {

    suspend fun getCurrentNasaImage() : LiveData<out ImageModel>
}