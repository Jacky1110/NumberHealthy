package com.jotangi.NumberHealthy.utils.smartwatch

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.jotangi.NumberHealthy.api.watch.WatchApiRepository
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.yucheng.ycbtsdk.Constants
import com.yucheng.ycbtsdk.YCBTClient
import timber.log.Timber
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean


val flagWorking: AtomicBoolean = AtomicBoolean(false)

open class SmartWatchBaseWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    protected var TAG = javaClass.simpleName
    protected var doneSignal: CountDownLatch? = null
    protected var doneSignal2: CountDownLatch? = null
    protected var isSuccess = false;
    protected var mac: String? = null
    protected var memberCode: String? = null

    protected val apiRepository: WatchApiRepository by lazy { WatchApiRepository() }

    override fun doWork(): Result {
        Log.d(TAG,"doWork, start")
        if (!flagWorking.compareAndSet(false, true)) {
            Log.d(TAG,"doWork, already running")
            return Result.success()
        }

        if (WatchUtils.instance.isBluetoothEnabled()) {
            mac = SharedPreferencesUtil.instances.getWatchMac()
            val dur = inputData.getLong("dur", 5)
            Log.w(TAG,"doWork, mac=$mac, dur=$dur")
            if (mac == null) {
                isSuccess = true
            } else {
                if (!isWatchConnected()) {
                    Log.w(TAG,"doWork, mac=$mac, watch disconnected")
                    isSuccess = false
                } else {
                    isSuccess = false;
                    memberCode = SharedPreferencesUtil.instances.getMemberCode()

                    getHistoryData()
                }
            }
        } else {
            isSuccess = false
        }

        scheduleNextWorker()
        if (isSuccess) {
            Log.w(TAG,"doWork success")
            flagWorking.compareAndSet(true, false)
            return Result.success()
        } else {
            Log.w(TAG,"doWork failure")
            flagWorking.compareAndSet(true, false)
            return Result.failure()
        }
    }


    protected open fun scheduleNextWorker() {
        WatchUtils.instance.initOneTimeWorker(10, TimeUnit.MINUTES)
    }

    protected open fun getHistoryData() {

    }

    protected open fun isWatchConnected(): Boolean {
        isSuccess = false
        val state = YCBTClient.connectState()
        if (state != Constants.BLEState.ReadWriteOK) {
            doneSignal = CountDownLatch(1)
            YCBTClient.disconnectBle()
            YCBTClient.connectBle(mac) { code ->
                Log.d(TAG,"doConnectDevice(), code=$code")
                if (code == Constants.CODE.Code_OK) {
                    isSuccess = true
                } else {
                }
                doneSignal!!.countDown()
            }
            doneSignal!!.await(5, TimeUnit.SECONDS)
        } else {
            isSuccess = true
        }
        return isSuccess
    }

    protected open fun delHistoryDataOnWatch(type: Int): Boolean {
        Log.d(TAG,"delHistoryDataOnWatch(), type=$type")
        isSuccess = false;
        doneSignal = CountDownLatch(1)
        YCBTClient.deleteHealthHistoryData(
            type
        ) { code, status, hashMap ->
            Log.d(TAG,"delHistoryDataOnWatch(), type=$type, code=$code")
            if (code == Constants.CODE.Code_OK) { //delete success
                isSuccess = true;
            }
            doneSignal?.countDown()
        }
        doneSignal!!.await(5, TimeUnit.SECONDS)

        return isSuccess
    }

    override fun onStopped() {
        super.onStopped()
        Log.d(TAG,"onStopped")
        flagWorking.compareAndSet(true, false)
    }
}