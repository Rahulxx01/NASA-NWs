package com.udacity.asteroidradar.repository

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters


class DatabaseWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {


    override fun doWork(): Result {



        return Result.success()

    }

}
