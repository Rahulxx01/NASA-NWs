package com.udacity.asteroidradar.dataImage

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.model.CurrentAsteroidResponse

interface ImageNetworkDataSource {
    val downloadImage : LiveData<ImageModel>

    suspend fun  fetechImage()
}