package com.udacity.asteroidradar

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.dataImage.ImageApiService
import com.udacity.asteroidradar.dataImage.ImageDatabase
import com.udacity.asteroidradar.dataImage.ImageNetworkDataSource
import com.udacity.asteroidradar.dataImage.ImageNetworkDataSourceImpl
import com.udacity.asteroidradar.databinding.FragmentDetailBinding.bind
import com.udacity.asteroidradar.db.AsteroidDatabase
import com.udacity.asteroidradar.main.AsteroidViewModelFactory
import com.udacity.asteroidradar.repository.AsteroidRepository
import com.udacity.asteroidradar.repository.AsteroidRepositoryImpl
import com.udacity.asteroidradar.repository.ImageRepository
import com.udacity.asteroidradar.repository.ImageRepositoryImpl
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


class AsteroidApplication : Application(),KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@AsteroidApplication))

        bind() from singleton { AsteroidDatabase(instance()) }
        bind() from singleton { instance<AsteroidDatabase >().asteroidDao() }

        bind() from singleton { ImageDatabase(instance()) }
        bind() from singleton { instance<ImageDatabase >().asteroidDao() }

        bind<ConnectivityInterceptor>() with singleton {  ConnectivityInterceptorImpl(instance())}


        bind() from singleton { AsteroidApiService(instance()) }
        bind() from singleton { ImageApiService(instance()) }

        bind<AsteroidNetworkDataSource>() with singleton {  AsteroidNetworkDataSourceImpl(instance())}
        bind<ImageNetworkDataSource>() with singleton {  ImageNetworkDataSourceImpl(instance())}



        bind<AsteroidRepository>() with singleton {  AsteroidRepositoryImpl(instance(),instance())}
        bind<ImageRepository>() with singleton {  ImageRepositoryImpl(instance(),instance())}

        bind()from provider {  AsteroidViewModelFactory(instance(),instance()) }

    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}