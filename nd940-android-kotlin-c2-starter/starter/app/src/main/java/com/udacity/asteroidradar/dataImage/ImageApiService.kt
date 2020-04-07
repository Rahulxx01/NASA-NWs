package com.udacity.asteroidradar.dataImage

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.udacity.asteroidradar.api.API_KEY
import com.udacity.asteroidradar.api.AsteroidApiService
import com.udacity.asteroidradar.api.ConnectivityInterceptor
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface ImageApiService {

    @GET("apod")
    fun getImageData(
    ): Deferred<ImageModel>


    companion   object{
        operator fun invoke (
            connectivityInterceptor: ConnectivityInterceptor
        ): ImageApiService {

            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("api_key",
                        "f0x6cl9rQhdeZ8IS2FQnzGG7IAkr4ZAxz7nqYIFX"
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
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.nasa.gov/planetary/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(ImageApiService::class.java )
        }
    }
}
