package com.udacity.asteroidradar.dataImage

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.udacity.asteroidradar.internal.NoConnectivityException


class ImageNetworkDataSourceImpl(
    private val asteroidApiService : ImageApiService
) : ImageNetworkDataSource {

    private val _downloadedImage = MutableLiveData<ImageModel>()
    override val downloadImage: LiveData<ImageModel>
        get() = _downloadedImage

    override suspend fun fetechImage() {
        try {
            val fetchCurrentAsteroid = asteroidApiService.getImageData().await()
            _downloadedImage.postValue(fetchCurrentAsteroid)
        }catch (e: NoConnectivityException){
            Log.e("Connectivity","No Internet Connection",e)
        }
    }


}