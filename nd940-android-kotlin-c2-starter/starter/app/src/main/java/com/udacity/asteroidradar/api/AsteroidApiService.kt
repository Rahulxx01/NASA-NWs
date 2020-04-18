package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.udacity.asteroidradar.dataImage.ImageModel
import com.udacity.asteroidradar.model.CurrentAsteroidResponse
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query



const val API_KEY = "f0x6cl9rQhdeZ8IS2FQnzGG7IAkr4ZAxz7nqYIFX"

//https://api.nasa.gov/neo/rest/v1/feed?start_date=START_DATE&end_date=END_DATE&api_key=YOUR_API_KEY
interface AsteroidApiService {


    //https://api.nasa.gov/neo/rest/v1/feed?start_date=2015-09-07&end_date=2015-09-08&api_key=DEMO_KEY
    //https://api.nasa.gov/neo/rest/v1/feed?start_date=2015-09-07&end_date=2015-09-08&key=f0x6cl9rQhdeZ8IS2FQnzGG7IAkr4ZAxz7nqYIFX
  //  f0x6cl9rQhdeZ8IS2FQnzGG7IAkr4ZAxz7nqYIFX
    @GET("feed")
    fun getAsteroidsGsonData(
        @Query("start_date") start_date : String,
        @Query("end_date") end_date : String
    ): Deferred<CurrentAsteroidResponse>


    @GET("feed")
    fun getAsteroidsMoshiData(
        @Query("start_date") start_date : String,
        @Query("end_date") end_date : String
    ): Call<CurrentAsteroidResponse>





    companion object{
        operator fun invoke (
            connectivityInterceptor: ConnectivityInterceptor
        ): AsteroidApiService {

            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key",
                        API_KEY
                    )
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)

            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                //.addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.nasa.gov/neo/rest/v1/")
                //.addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(AsteroidApiService::class.java )
        }
    }
}