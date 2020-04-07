package com.udacity.asteroidradar.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.ImageRepository

class AsteroidViewModelFactory(
    private val asteroidRepository: AsteroidRepository,
    private val imageRepository: ImageRepository
) : ViewModelProvider.NewInstanceFactory()  {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(asteroidRepository,imageRepository) as T
    }
}