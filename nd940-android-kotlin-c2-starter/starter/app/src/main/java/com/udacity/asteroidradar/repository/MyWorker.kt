package com.udacity.asteroidradar.repository

import android.content.Context
import android.database.Observable
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.impl.Schedulers
import com.google.gson.Gson
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.ApiService
import com.udacity.asteroidradar.api.ServiceBuilder
import com.udacity.asteroidradar.api.getNextSevenDaysFormattedDates
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.db.AsteroidDatabase
import com.udacity.asteroidradar.model.CurrentAsteroidResponse
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.rxjava3.observers.TestObserver.create
import io.reactivex.schedulers.Schedulers.io
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URI.create


class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams){




    override  fun doWork(): Result {
//
       val destinationService =  ServiceBuilder.buildService(ApiService::class.java )
       // val destinationService =  ServiceBuilder.buildService(ServiceBuilder::class.java)
        val dateList = getNextSevenDaysFormattedDates()
        val requestCall =   destinationService.getAsteroidsMoshiData(dateList[0].toString(),dateList[dateList.size-1].toString())
        requestCall.enqueue(object : Callback<CurrentAsteroidResponse> {

            override fun onFailure(call: Call<CurrentAsteroidResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

            override fun onResponse(
                call: Call<CurrentAsteroidResponse>,
                response: Response<CurrentAsteroidResponse>
            ) {
                val currentAsteriodResponse = response.body()!!
                val jsonInString = Gson().toJson(currentAsteriodResponse.near_earth_objects)
                val mJSONObject = JSONObject(jsonInString)
                val asteroidList  = parseAsteroidsJsonResult(mJSONObject)
                //AsteroidDatabase(applicationContext).asteroidDao().upsert(asteroidList)
                val dataBaseRunnable  = DatabaseRunnable(asteroidList)
                Thread(dataBaseRunnable).start()


            }

        })





        return Result.success()

    }



  inner class DatabaseRunnable(asteriodList: ArrayList<Asteroid>) : Runnable {
       val list = asteriodList
       override fun run() {
           AsteroidDatabase(applicationContext).asteroidDao().upsert(list)

       }

   }






}




