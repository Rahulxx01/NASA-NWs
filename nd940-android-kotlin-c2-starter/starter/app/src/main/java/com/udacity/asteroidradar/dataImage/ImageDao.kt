package com.udacity.asteroidradar.dataImage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(asteriod : ImageModel)

    @Query("select * from  current_image")
    fun getImageSingleData(): LiveData<ImageModel>

    @Query("select * from  current_image where id = $CURRENT_IMAGE_ID" )
    fun getImageData(): LiveData<ImageModel>



    @Query("delete  from  current_image")
    fun deleteOldAsteroidData()


}