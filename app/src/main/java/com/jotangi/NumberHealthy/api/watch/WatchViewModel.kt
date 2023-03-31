package com.jotangi.NumberHealthy.api.watch

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jotangi.NumberHealthy.ui.ecg.EcgDataCallback
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.jotangi.NumberHealthy.utils.constant.RangeBodyTem
import com.yucheng.ycbtsdk.AITools
import com.yucheng.ycbtsdk.Constants
import com.yucheng.ycbtsdk.YCBTClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class WatchViewModel(var watchApiRepository: WatchApiRepository) : ViewModel() {

    private val TAG: String = "${javaClass.simpleName}(TAG)"

    val isBleConnect = MutableLiveData(false)
    val watchBatteryLevel = MutableLiveData(100)
    val headshotPath = MediatorLiveData<String>()
    val upDataTime = MediatorLiveData<String>()

    // 07. App 取得步數資料
    val dayFootStepsData = MediatorLiveData<List<GetStepsBean>>()

    // 08. App 取得睡眠數據
    val sleepDayTotalDataLD = MediatorLiveData<List<SleepDayTotalData>>()

    // 09. App 取得睡眠細節
    val sleepHorViewLD = MediatorLiveData<List<SleepDataBean>>()

    // 10. App 取得心率數值
    val lastHeartRateBean = MediatorLiveData<List<HeartRateBean>>()

    // 11. App 取得血壓數值
    val lastBpBean = MediatorLiveData<List<BPBean>>()

    // 13. App 取得血氧數值
    val lastOxygenLD = MediatorLiveData<List<OxygenBean>>()

    // 15. App 取得ECG數值
    val last7EcgBean = MediatorLiveData<List<EcgBean>>()

    // 17. App 取得呼吸率數值
    val lastBreathRateBean = MediatorLiveData<List<BreathRateBean>>()

    // 19. App 取得體溫數值
    val lastTemperatureLD = MediatorLiveData<List<TemperatureBean>>()

    // 22.App 取得骨密的檢查數值
    val getBmdList = MediatorLiveData<List<GetBmdBean>>()

    // 24.App 取得黃斑部色素檢查數值
    val getMpodList = MediatorLiveData<List<GetMpodBean>>()

    // 26.App 取得雙臂血壓數值
    val getBp2List = MediatorLiveData<List<GetBp2Bean>>()

    // 28. App 取得卡路里的數值
    val getKcalList = MediatorLiveData<List<GetKcalDataBean>>()

    /**
     * 07. App 取得步數資料
     */
    suspend fun getGetSteps(request: ElementRequest) {

        var data = watchApiRepository.getSteps(request).data
        /**
         * [FootStepsData(sportStartTime=2022-05-31 12:00:00, sportEndTime=2022-05-31 12:30:00, sportStep=112, sportCalorie=5, sportDistance=102),
         * FootStepsData(sportStartTime=2022-05-31 12:30:00, sportEndTime=2022-05-31 13:00:00, sportStep=1674, sportCalorie=68, sportDistance=1188),
         * FootStepsData(sportStartTime=2022-05-31 14:00:00, sportEndTime=2022-05-31 14:30:00, sportStep=1528, sportCalorie=62, sportDistance=1085)]
         */

        if (data.isNullOrEmpty()) return

        data = data.sortedByDescending { it.sportStartTime }

        val buildList = mutableListOf<GetStepsBean>()

        for (i in data.indices) {

            if (buildList.isEmpty() ||
                data[i].sportStartTime == null ||
                !data[i].sportStartTime!!.contains(
                    DateUtil.instance.clipTimeToYMD(buildList.last().sportStartTime)
                )
            ) {
                data[i].sportStartTime ?: continue
                buildList.add(data[i])
                continue
            }

            buildList.last().apply {

                sportStartTime = data[i].sportStartTime
                sportStep = ((sportStep?.toInt() ?: 0) +
                        (data[i].sportStep?.toInt() ?: 0)).toString()
                sportCalorie = ((sportCalorie?.toInt() ?: 0) +
                        (data[i].sportCalorie?.toInt() ?: 0)).toString()
                sportDistance = ((sportDistance?.toInt() ?: 0) +
                        (data[i].sportDistance?.toInt() ?: 0)).toString()
            }
        }
        Log.w(TAG, "DayList: $buildList")
        dayFootStepsData.postValue(buildList)
    }

    /**
     * 08. App 取得睡眠數據
     */
    suspend fun getSleep(request: ElementRequest) {

        /**
         * [SleepData(startTime=2022-07-10 04:30:08, endTime=2022-07-10 08:33:24, deepSleepCount=1, lightSleepCount=10, deepSleepTotal=47, lightSleepTotal=195),
         * SleepData(startTime=2022-07-07 04:25:26, endTime=2022-07-07 08:23:08, deepSleepCount=3, lightSleepCount=8, deepSleepTotal=71, lightSleepTotal=166),
         * SleepData(startTime=2022-07-07 01:35:35, endTime=2022-07-07 03:47:28, deepSleepCount=1, lightSleepCount=5, deepSleepTotal=39, lightSleepTotal=92),
         * SleepData(startTime=2022-07-06 07:01:31, endTime=2022-07-06 09:02:01, deepSleepCount=0, lightSleepCount=6, deepSleepTotal=0, lightSleepTotal=120),
         * SleepData(startTime=2022-07-06 00:49:41, endTime=2022-07-06 06:44:20, deepSleepCount=3, lightSleepCount=13, deepSleepTotal=68, lightSleepTotal=286)]
         * deepSleepTotal、lightSleepTotal:分鐘
         */

        watchApiRepository.getSleep(request).data?.let { list ->

            if (list.isEmpty()) return

            val data = list.sortedByDescending { it.startTime }

            var baseDate = DateUtil.instance.calculationBaseDate(
                data[0].startTime.toString()
            )

            val sleepTotalList = arrayListOf<SleepDayTotalData>()
            sleepTotalList.add(SleepDayTotalData())

            for (i in data.indices) {

                if (data[i].startTime.toString() < baseDate) {
                    baseDate = DateUtil.instance.calculationBaseDate(
                        data[i].startTime.toString()
                    )
                    sleepTotalList.add(SleepDayTotalData())
                }

                with(sleepTotalList.last()) {

                    if (endTime == null) {
                        endTime = data[i].endTime ?: ""
                    }

                    deepMinuteTotal += data[i].deepSleepTotal?.toInt() ?: 0
                    lightMinuteTotal += data[i].lightSleepTotal?.toInt() ?: 0
                    startTime = data[i].startTime
                }
            }

            Log.d(TAG, "sleepTotalList: $sleepTotalList")
            sleepDayTotalDataLD.postValue(sleepTotalList)

            getSleepData(
                ElementRequest(
                    request.memberId,
                    sleepTotalList.first().startTime!!,
                    sleepTotalList.first().endTime!!
                )
            )
        }
    }

    /**
     * 09. App 取得睡眠細節
     */
    private suspend fun getSleepData(request: ElementRequest) {

        /**
         * [SleepDetailData(sleepStartTime=2022-07-10 07:08:14, sleepType=242, sleepLen=5110),
         * SleepDetailData(sleepStartTime=2022-07-10 06:20:50, sleepType=241, sleepLen=2843),
         * SleepDetailData(sleepStartTime=2022-07-10 04:30:08, sleepType=242, sleepLen=6641),
         * SleepDetailData(sleepStartTime=2022-07-07 07:58:12, sleepType=241, sleepLen=1496),
         * SleepDetailData(sleepStartTime=2022-07-07 07:24:38, sleepType=242, sleepLen=2013),
         * SleepDetailData(sleepStartTime=2022-07-07 06:48:00, sleepType=241, sleepLen=2197),
         * SleepDetailData(sleepStartTime=2022-07-07 04:57:47, sleepType=242, sleepLen=6612),
         * SleepDetailData(sleepStartTime=2022-07-07 04:48:06, sleepType=241, sleepLen=580),
         * SleepDetailData(sleepStartTime=2022-07-07 04:25:26, sleepType=242, sleepLen=1359),
         * SleepDetailData(sleepStartTime=2022-07-07 03:07:56, sleepType=241, sleepLen=2372),
         * SleepDetailData(sleepStartTime=2022-07-07 01:35:35, sleepType=242, sleepLen=5540),
         * SleepDetailData(sleepStartTime=2022-07-06 07:01:31, sleepType=242, sleepLen=7230),
         * SleepDetailData(sleepStartTime=2022-07-06 06:22:06, sleepType=242, sleepLen=1334),
         * SleepDetailData(sleepStartTime=2022-07-06 05:47:50, sleepType=241, sleepLen=2055),
         * SleepDetailData(sleepStartTime=2022-07-06 02:43:27, sleepType=242, sleepLen=11062),
         * SleepDetailData(sleepStartTime=2022-07-06 02:27:52, sleepType=241, sleepLen=934),
         * SleepDetailData(sleepStartTime=2022-07-06 01:51:26, sleepType=242, sleepLen=2185),
         * SleepDetailData(sleepStartTime=2022-07-06 01:32:56, sleepType=241, sleepLen=1109),
         * SleepDetailData(sleepStartTime=2022-07-06 00:49:41, sleepType=242, sleepLen=2594)]
         * sleepType: 242(深睡)、241(淺睡)
         * sleepLen: 秒數
         */

        watchApiRepository.getSleepData(request).data?.let { list ->

            if (list.isEmpty()) return

            sleepHorViewLD.postValue(
                list.sortedByDescending { it.sleepStartTime }
            )
        }
    }

    /**
     * 10. App 取得心率數值
     */
    suspend fun getHeartRate(request: ElementRequest) {

        watchApiRepository.getHeartRate(request).data?.let { list ->

            if (list.isEmpty()) return

            lastHeartRateBean.postValue(
                list.sortedByDescending { it.heartStartTime }
            )
        }
    }

    /**
     * 11. App 取得血壓數值
     */
    suspend fun getBp(request: ElementRequest) {

        watchApiRepository.getBP(request).data?.let { list ->

            if (list.isEmpty()) return

            lastBpBean.postValue(
                list.sortedByDescending { it.bloodStartTime }
            )
        }
    }

    /**
     * 13. App 取得血氧數值
     */
    suspend fun getOxygen(request: ElementRequest) {

        watchApiRepository.getOxygen(request).data?.let { list ->

            if (list.isEmpty()) return

            lastOxygenLD.postValue(
                list.sortedByDescending { it.startTime }
            )
        }
    }

    /**
     * 15. App 取得ECG數值
     */
    suspend fun getECG(request: ElementRequest) {

        watchApiRepository.getECG(request).data?.let { list ->

            if (list.isEmpty()) return

            last7EcgBean.postValue(
                list.sortedByDescending { it.ecgStartTime }
            )
        }
    }

    /**
     * 17. App 取得呼吸率數值
     */
    suspend fun getBreathRate(request: ElementRequest) {

        watchApiRepository.getBreathRate(request).data?.let { list ->

            if (list.isEmpty()) return

            lastBreathRateBean.postValue(
                list.sortedByDescending { it.startTime }
            )
        }
    }

    /**
     * 19. App 取得體溫數值
     */
    suspend fun getTemperature(request: ElementRequest) {

        watchApiRepository.getTemperature(request).data?.let { list ->

            if (list.isEmpty()) return

            val array = list.filter {
                RangeBodyTem.Mini.value < it.temperature.toFloat() &&
                        it.temperature.toFloat() < RangeBodyTem.Max.value
            }

            lastTemperatureLD.postValue(
                array.sortedByDescending { it.startTime }
            )
        }
    }

    /**
     * 22.App 取得骨密的檢查數值
     */
    suspend fun getBMD(request: ElementRequest) {

        watchApiRepository.getBMD(request).data?.let { list ->

            if (list.isEmpty()) return

            getBmdList.postValue(
                list.sortedByDescending { it.startTime }
            )
        }
    }

    /**
     * 24.App 取得黃斑部色素檢查數值
     */
    suspend fun getMPOD(request: ElementRequest) {

        watchApiRepository.getMPOD(request).data?.let { list ->

            if (list.isEmpty()) return

            getMpodList.postValue(
                list.sortedByDescending { it.mpodStartTime }
            )
        }
    }

    /**
     * 26.App 取得雙臂血壓數值
     */
    suspend fun getBp2(request: ElementRequest) {

        watchApiRepository.getBp2(request).data?.let { list ->

            if (list.isEmpty()) return

            getBp2List.postValue(
                list.sortedByDescending { it.bloodStartTime }
            )
        }
    }

    /**
     * 28. App 取得卡路里的數值
     */
    suspend fun getKcal(request: ElementRequest) {

        val data = watchApiRepository.getKcal(request).data

        // [{"startTime":"2023-03-03 16:55:00","KCAL":"100","dataType":"1"}]}

        if (data.isNullOrEmpty() && dayFootStepsData.value.isNullOrEmpty()) return

        val kcals = ArrayList<GetKcalDataBean>()
        kcals.addAll(data ?: arrayListOf())
        var steps = dayFootStepsData.value
        if (steps.isNullOrEmpty()) steps = mutableListOf()

        for (i in steps.indices) {
            kcals.add(
                GetKcalDataBean(
                    steps[i].sportStartTime,
                    steps[i].sportCalorie
                )
            )
        }

        kcals.sortedByDescending { it.startTime }.let {
            kcals.clear()
            kcals.addAll(it)
        }

        val buildList = mutableListOf<GetKcalDataBean>()

        for (i in kcals.indices) {

            if (buildList.isEmpty() ||
                kcals[i].startTime == null ||
                !kcals[i].startTime!!.contains(
                    DateUtil.instance.clipTimeToYMD(buildList.last().startTime)
                )
            ) {
                kcals[i].startTime ?: continue
                buildList.add(kcals[i])
                continue
            }

            buildList.last().apply {

                KCAL = ((KCAL?.toInt() ?: 0) +
                        (kcals[i].KCAL?.toInt() ?: 0)).toString()
            }
        }
        Log.w(TAG, "DayList: $buildList")
        getKcalList.postValue(buildList)
    }


    fun setUpDataTime(time: String) {
        upDataTime.postValue(time)
    }


    fun refreshHeadShotPath() {
        headshotPath.postValue(SharedPreferencesUtil.instances.getAccountHeadShot())
    }

    fun setIsBleConnect(boolean: Boolean) {
        isBleConnect.postValue(boolean)
    }

    fun getWatchInfo() {

        CoroutineScope(Dispatchers.IO).launch {

            val state = YCBTClient.connectState()
            if (state == Constants.BLEState.ReadWriteOK) {
                doGetDeviceInfo()
            } else {
                val mac = SharedPreferencesUtil.instances.getWatchMac()
                if (!mac.isNullOrBlank()) {
                    YCBTClient.disconnectBle()
                    YCBTClient.connectBle(mac) { code ->
                        if (code == Constants.CODE.Code_OK) {
                            doGetDeviceInfo()
                        }
                    }
                }
            }
        }
    }

    private fun doGetDeviceInfo() {
        YCBTClient.getDeviceInfo { code, ratio, resultMap ->
            if (resultMap != null) {
                val dataObj = resultMap.get("data")
                if (dataObj is Map<*, *>) {
                    val level: Int = dataObj.get("deviceBatteryValue") as Int
                    setWatchBatteryLevel(level)
                }
            }
        }
    }

    fun setWatchBatteryLevel(level: Int) {
        watchBatteryLevel.postValue(level)
    }

    fun appECGTest(callback: EcgDataCallback.Start) {
        val aitool = AITools.getInstance()
        aitool.init()
        aitool.setAIDiagnosisHRVNormResponse {

        }
        YCBTClient.appEcgTestStart({ i, fl, hashMap ->
        }, { i, hashMap ->
            hashMap.let {
                try {
                    when (i) {
                        Constants.DATATYPE.Real_UploadECG -> {
                            val tData = hashMap["data"] as ArrayList<Int>
                            //Log.d("eee",tData.toString())
                            //var aa = aitool.ecgRealWaveFiltering(listToBytes(tData))
                            for (i in 0 until tData.size) {
                                callback.receiveECG(tData[i].toFloat())
                            }
                        }
                        Constants.DATATYPE.Real_UploadECGHrv -> {
                            val tData = hashMap["data"] as Float
                            callback.receiveHRV(tData.toInt())
                        }
                        Constants.DATATYPE.Real_UploadBlood -> {
                            val heart = hashMap["heartValue"] as Int
                            val tDBP = hashMap["bloodDBP"] as Int
                            val tSBP = hashMap["bloodSBP"] as Int
                            callback.receiveBlood(heart, tDBP, tSBP)
                        }
                        else -> {
                            callback.receiveBadSignal()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    fun appECGTestEnd(callback: EcgDataCallback.End) {
        try {
            YCBTClient.appEcgTestEnd { i, fl, hashMap ->
                callback.receive(fl)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}