package com.udacity.asteroidradar.dataImage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.db.AsteroidDao

@Database(
    entities = [ImageModel::class],
    version = 1
)
abstract class ImageDatabase : RoomDatabase() {
    abstract fun asteroidDao() : ImageDao

    companion object {
        @Volatile private var instance : ImageDatabase ? = null
        private val LOCK = Any()
        operator fun invoke(context : Context) = instance ?: synchronized(LOCK){
            instance?: buildDatabase(context).also{
                instance = it
            }

        }
        private fun buildDatabase(context: Context) = Room.databaseBuilder(context.applicationContext,ImageDatabase::class.java,"nasaImage.db")
            .build()

    }
}