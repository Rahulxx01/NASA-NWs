package com.udacity.asteroidradar.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.Asteroid

@Database(
    entities = [Asteroid::class],
    version = 1
)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract fun asteroidDao() : AsteroidDao

    companion object {
        @Volatile private var instance : AsteroidDatabase ? = null
        private val LOCK = Any()
        operator fun invoke(context : Context) = instance ?: synchronized(LOCK){
            instance?: buildDatabase(context).also{
                instance = it
            }

        }
        private fun buildDatabase(context: Context) = Room.databaseBuilder(context.applicationContext,AsteroidDatabase::class.java,"asteroid.db")
            .build()

    }
}