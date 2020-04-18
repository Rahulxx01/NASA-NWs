package com.udacity.asteroidradar.dataImage

import androidx.room.Entity
import androidx.room.PrimaryKey

const val CURRENT_IMAGE_ID = 0

@Entity(tableName = "current_image")
data class ImageModel(

    val url: String,
    val media_type :String
){
    @PrimaryKey(autoGenerate = false)
    var id : Int = CURRENT_IMAGE_ID
}