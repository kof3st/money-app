package me.kofesst.android.moneyapp.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import me.kofesst.android.moneyapp.database.MainDatabase

class SubscriptionsWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    private val database: MainDatabase = MainDatabase.get(context)

    override fun doWork(): Result {
        Log.d("AAA", "Work!")
        return Result.success()
    }

}