package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.work.WorkManager
import com.google.gson.Gson
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.AsteroidNetworkDataSource
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.db.AsteroidDao
import com.udacity.asteroidradar.model.CurrentAsteroidResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


open class AsteroidRepositoryImpl(
    private val asteroidDao: AsteroidDao,
    private val asteroidNetworkDataSource: AsteroidNetworkDataSource
) : AsteroidRepository {



    init {
        asteroidNetworkDataSource.downloadedCurrentAsteroids.observeForever {
            newCurrentAsteroid -> persistFetchCurrentAsteroid(newCurrentAsteroid)
        }
    }
    override suspend fun getAsteroidData(): LiveData<out List<Asteroid>> {
        initAsteroidData()
        return withContext(Dispatchers.IO) {

            val dtf: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val now: LocalDateTime = LocalDateTime.now()
            asteroidDao.getAsteroidData(now.toString())
        }
    }


    public fun persistFetchCurrentAsteroid(fetchedAsteroid :  CurrentAsteroidResponse){

        fun deleteForeCastAsteroidData(){
            asteroidDao.deleteOldAsteroidData()
        }

        GlobalScope.launch(Dispatchers.IO) {
            deleteForeCastAsteroidData()

            val jsonInString = Gson().toJson(fetchedAsteroid.near_earth_objects)
            val mJSONObject = JSONObject(jsonInString)
           val asteroidList  = parseAsteroidsJsonResult(mJSONObject)
            asteroidDao.upsert(asteroidList)

        }
    }

    public suspend fun initAsteroidData(){
        if(isFetechCurrentNeeded(ZonedDateTime.now())){
                fetchCurrentAsteroid()
        }
    }

    public suspend fun fetchCurrentAsteroid(){
        val dateList = getNextSevenDaysFormattedDates()
        asteroidNetworkDataSource.fetechCurrentAsteroids(dateList[0].toString(),dateList[dateList.size-1].toString());
    }

    public fun isFetechCurrentNeeded(lastFetechTime : ZonedDateTime) : Boolean {
        val oneDayAgo = ZonedDateTime.now().minusDays(1)
        return lastFetechTime.isAfter(oneDayAgo)
    }

    fun objectToJSONObject(`object`: Any): JSONObject? {
        var json: Any? = null
        var jsonObject: JSONObject? = null
        try {
            json = JSONTokener(`object`.toString()).nextValue()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (json is JSONObject) {
            jsonObject = json
        }
        return jsonObject
    }

}