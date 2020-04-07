package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.dataImage.ImageDao
import com.udacity.asteroidradar.dataImage.ImageModel
import com.udacity.asteroidradar.dataImage.ImageNetworkDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.ZonedDateTime

class ImageRepositoryImpl(
    private val imageDao: ImageDao,
    private val imageNetworkDataSource: ImageNetworkDataSource
) : ImageRepository {


    init {
        imageNetworkDataSource.downloadImage.observeForever { newCurrentImage ->
            persistFetchedCurrentImage(newCurrentImage)
        }
    }


    private fun persistFetchedCurrentImage(fetchImage : ImageModel){
        GlobalScope.launch(Dispatchers.IO) {
            imageDao.upsert(fetchImage)

        }

    }
    override suspend fun getCurrentNasaImage(): LiveData<out ImageModel> {
        initAsteroidData()
       return withContext(Dispatchers.IO){
           return@withContext imageDao.getImageData()
       }
    }



    private suspend fun initAsteroidData(){
        if(isFetechCurrentNeeded(ZonedDateTime.now())){
            fetchCurrentAsteroid()
        }
    }

    private suspend fun fetchCurrentAsteroid(){
        //val dateList = getNextSevenDaysFormattedDates()
        imageNetworkDataSource.fetechImage();
    }

    private fun isFetechCurrentNeeded(lastFetechTime : ZonedDateTime) : Boolean {
        val oneDayAgo = ZonedDateTime.now().minusDays(1)
        return lastFetechTime.isAfter(oneDayAgo)
    }
}