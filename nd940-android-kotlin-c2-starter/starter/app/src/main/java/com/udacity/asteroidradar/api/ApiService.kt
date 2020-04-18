package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.model.CurrentAsteroidResponse
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query



//https://api.nasa.gov/neo/rest/v1/feed?start_date=START_DATE&end_date=END_DATE&api_key=YOUR_API_KEY
interface ApiService {


    //https://api.nasa.gov/neo/rest/v1/feed?start_date=2015-09-07&end_date=2015-09-08&api_key=DEMO_KEY
    //https://api.nasa.gov/neo/rest/v1/feed?start_date=2015-09-07&end_date=2015-09-08&key=f0x6cl9rQhdeZ8IS2FQnzGG7IAkr4ZAxz7nqYIFX
    //  f0x6cl9rQhdeZ8IS2FQnzGG7IAkr4ZAxz7nqYIFX
    @GET("feed")
    fun getAsteroidsGsonData(
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String
    ): Deferred<CurrentAsteroidResponse>


    @GET("feed")
    fun getAsteroidsMoshiData(
        @Query("start_date") start_date: String,
        @Query("end_date") end_date: String
    ): Call<CurrentAsteroidResponse>


}