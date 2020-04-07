package com.udacity.asteroidradar.main

import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.internal.lazyDeferred
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.ImageRepository

class MainViewModel(private val asteroidRepository: AsteroidRepository,private val imageRepository: ImageRepository) : ViewModel() {

       val asteroidList by lazyDeferred {
           asteroidRepository.getAsteroidData()
       }


    val imageNASA by lazyDeferred {
        imageRepository.getCurrentNasaImage()
    }

}