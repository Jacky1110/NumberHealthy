package com.jotangi.NumberHealthy.utils.smartwatch

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.yucheng.ycbtsdk.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch

open class WatchWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    private val TAG = "${javaClass.simpleName}(TAG)"

    private var isSuccess = true

    override fun doWork(): Result {
        Log.w(TAG, "doWork: 21")
        asyncWatchData()

        Log.w(TAG, "doWork isSuccess: $isSuccess")
        return if (isSuccess)
            Result.success()
        else
            Result.failure()
    }


    private fun asyncWatchData() {
        val doneSignal: CountDownLatch?
//        var dataType = intArrayOf(
//            Constants.DATATYPE.Health_HistoryHeart,
//            Constants.DATATYPE.Health_HistoryAll,
//            Constants.DATATYPE.Health_HistoryBlood,
//            Constants.DATATYPE.Health_HistorySport,
//            Constants.DATATYPE.Health_HistorySleep
//        )
        Log.w(TAG, "asyncWatchData: 41")
        doneSignal = CountDownLatch(1)
        CoroutineScope(Dispatchers.IO).launch {
            try {
//                for (type in dataType) {
//                    WatchUtils.instance.asyncHealthHistoryData(type)
//                }
                WatchUtils.instance.asyncHealthHistoryData(Constants.DATATYPE.Health_HistoryHeart)
                WatchUtils.instance.asyncHealthHistoryData(Constants.DATATYPE.Health_HistoryAll)
                WatchUtils.instance.asyncHealthHistoryData(Constants.DATATYPE.Health_HistoryBlood)
                WatchUtils.instance.asyncHealthHistoryData(Constants.DATATYPE.Health_HistorySport)
                WatchUtils.instance.asyncHealthHistoryData(Constants.DATATYPE.Health_HistorySleep)
                WatchUtils.instance.asyncHealthHistoryData(Constants.DATATYPE.AppTodayWeather)

                doneSignal.countDown()
                isSuccess = true
            } catch (e: Exception) {
                e.printStackTrace()
                doneSignal.countDown()
                isSuccess = false
            }
        }
        doneSignal.await()
    }

}