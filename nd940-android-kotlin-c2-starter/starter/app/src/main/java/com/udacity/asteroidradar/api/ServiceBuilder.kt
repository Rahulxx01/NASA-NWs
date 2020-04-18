package com.udacity.asteroidradar.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ServiceBuilder {




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

    private val okHttp = OkHttpClient.Builder().addInterceptor(requestInterceptor)

    private val builder = Retrofit.Builder()
        .baseUrl("https://api.nasa.gov/neo/rest/v1/")
        .addConverterFactory(MoshiConverterFactory.create())

        .client(okHttp.build())


    private val retrofit = builder.build()




    fun<T> buildService(serviceType : Class<T>): T {
        return retrofit.create(serviceType)
    }



}