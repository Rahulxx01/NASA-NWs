package com.udacity.asteroidradar.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.internal.NoConnectivityException
import com.udacity.asteroidradar.model.CurrentAsteroidResponse
import retrofit2.await

class AsteroidNetworkDataSourceImpl(
    private val asteroidApiService : AsteroidApiService
) : AsteroidNetworkDataSource {

    private val _downloadedCurrentAsteroids = MutableLiveData<CurrentAsteroidResponse>()
    override val downloadedCurrentAsteroids: LiveData<CurrentAsteroidResponse>
        get() = _downloadedCurrentAsteroids

    override suspend fun fetechCurrentAsteroids(start_date: String, end_date: String) {
        try {
           val fetchCurrentAsteroid = asteroidApiService.getAsteroidsMoshiData(start_date,end_date).await()
            _downloadedCurrentAsteroids.postValue(fetchCurrentAsteroid)
        }catch (e:NoConnectivityException){
            Log.e("Connectivity","No Internet Connection",e)
        }
    }
}