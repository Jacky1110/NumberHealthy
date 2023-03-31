package com.jotangi.NumberHealthy.utils.smartwatch

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.google.gson.Gson
import com.jotangi.NumberHealthy.api.ApiConnect
import com.jotangi.NumberHealthy.api.watch.*
import com.jotangi.NumberHealthy.utils.AppUtil
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.jotangi.NumberHealthy.utils.constant.RangeBodyTem
import com.jotangi.NumberHealthy.utils.smartwatch.model.*
import com.yucheng.ycbtsdk.Constants
import com.yucheng.ycbtsdk.YCBTClient
import com.yucheng.ycbtsdk.response.BleConnectResponse
import com.yucheng.ycbtsdk.response.BleDataResponse
import com.yucheng.ycbtsdk.response.BleScanResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class WatchUtils private constructor() {

    private val TAG = "${javaClass.simpleName}(TAG)"

    companion object {
        val instance by lazy { WatchUtils() }
    }

    private lateinit var appContext: Context
    protected var doneSignal: CountDownLatch? = null  //確保手錶數據上傳排程順序
    private val KEY_SYNC_WATCH_WORKER_NAME = "numhealthy_watchperiod"
    protected val apiRepository: WatchApiRepository by lazy { WatchApiRepository() }
    private val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())


    /**
     * init
     */
    fun initWatchSDK(context: Context) {
        appContext = context
        YCBTClient.initClient(context, true)
    }

    fun deviceToApp() {
        YCBTClient.deviceToApp { dataType, dataMap ->
            Log.w("deviceToApp", dataMap.toString())
        }
    }


    /**
     * boolean
     */
    fun isBluetoothEnabled(): Boolean {
        val bleAdapter = BluetoothAdapter.getDefaultAdapter()
        return (bleAdapter != null && bleAdapter.isEnabled)
    }

    fun isBleConnected(): Boolean {
        return SharedPreferencesUtil.instances.getWatchName() != null &&
                SharedPreferencesUtil.instances.getWatchMac() != null &&
                YCBTClient.connectState() == Constants.BLEState.ReadWriteOK
    }

    fun isConnectBle(): Boolean {
        val stateCode = YCBTClient.connectState()
        Log.e(TAG, "ble state - Code: $stateCode")
        return stateCode > 4
    }

    fun isBleConnect(name: String, mac: String, success: () -> Unit, fail: () -> Unit) {

        val stateCode = YCBTClient.connectState()
        Log.d(TAG, "ble state Code: $stateCode")

        if (stateCode == Constants.BLEState.ReadWriteOK) {

            Log.w(TAG, "藍牙 name: $name mac: $mac -> 成功")
            success()

        } else {

            Log.e(TAG, "藍牙 name: $name mac: $mac -> 失敗")
            SharedPreferencesUtil.instances.setWatchName("")
            SharedPreferencesUtil.instances.setWatchMac("")
            fail()
        }
    }


    /**
     * ble 連線
     */
    fun startScanBle(response: BleScanResponse, timeOut: Int) {
        YCBTClient.startScanBle(response, timeOut)
    }

    fun stopScanBle() {
        YCBTClient.stopScanBle()
    }

    fun connectBle(mac: String, response: BleConnectResponse) {
        Log.d(TAG, "嘗試藍牙連線 mac: $mac")
        YCBTClient.connectBle(mac, response)
    }

    fun disconnectBle() {
        YCBTClient.disconnectBle()
    }

    fun isScanning(): Boolean {
        return YCBTClient.isScaning()
    }

    fun getDeviceInfo(response: BleDataResponse) {
        YCBTClient.getDeviceInfo(response)
    }

    fun settingHandWear(isRight: Boolean, response: BleDataResponse) {

        if (isRight) {
            YCBTClient.settingHandWear(0x01, response)
        } else {
            YCBTClient.settingHandWear(0x00, response)
        }
    }

//    fun getDeviceUserConfig(response: BleDataResponse) {
//        Log.d("eee", "getElectrodeLocationInfo_$response")
//        YCBTClient.getDeviceUserConfig(response)
//    }

    fun getHealthHistoryData(type: Int, response: BleDataResponse) {
        Log.d(TAG, "getHealthHistoryData_$type")
        YCBTClient.healthHistoryData(type, response)
    }

    suspend fun uploadHealthHistoryData(
        type: Int,
        data: List<Any>
    ): Boolean { //只要有一筆上傳失敗就不刪手錶資料 等待下一次
        val memberId = SharedPreferencesUtil.instances.getAccountId()
        if (data.isEmpty() || memberId == null) {
            return false
        }

        when (type) {
            Constants.DATATYPE.Health_HistorySleep -> {
                val list = data as List<HistorySleepInfo>

                val lastTime = SharedPreferencesUtil.instances.getWatchLongValue(
                    SharedPreferencesUtil.instances.KEY_LAST_UPLOADD_SLEEP_TIME
                )
                var newlastTime = 0L
                var uploadStatus = true
                for (item in list) {
                    if (item.startTime > lastTime) {
                        val starttime = sdf.format(Date(item.startTime))
                        val endtime = sdf.format(Date(item.endTime))
                        val sleepdata = Gson().toJson(item.sleepData)

//                        val strSleepLen = item.endTime - item.startTime
//                        val sleepLen = TimeUnit.MILLISECONDS.toSeconds(strSleepLen).toString()

                        Log.v(TAG, AppUtil.instance.title("上傳睡眠"))
                        val resp = apiRepository.uploadSleep(
                            UploadSleepRequest(
                                memberId,
                                starttime, endtime,
                                item.deepSleepCount,
                                item.lightSleepCount,
                                item.deepSleepTotal / 60,
                                item.lightSleepTotal / 60,
                                sleepdata
                            )
                        )

                        if (!resp.code.equals("0x0200"))
                            uploadStatus = false
                        else {
                            if (item.startTime > newlastTime)
                                newlastTime = item.startTime
                        }
                    }
                }
                if (newlastTime > lastTime) {
                    SharedPreferencesUtil.instances.setWatchLongValue(
                        SharedPreferencesUtil.instances.KEY_LAST_UPLOADD_SLEEP_TIME,
                        newlastTime
                    )
                }

                return uploadStatus
            }

            Constants.DATATYPE.Health_HistoryHeart -> {
                val list = data as List<HistoryHeartInfo>
                val lastTime = SharedPreferencesUtil.instances.getWatchLongValue(
                    SharedPreferencesUtil.instances.KEY_LAST_UPLOADD_HR_TIME
                )
                var newlastTime = 0L
                var uploadStatus = true
                for (item in list) {
                    if (item.heartStartTime > lastTime) {
                        val startTime = sdf.format(Date(item.heartStartTime))

                        Log.v(TAG, AppUtil.instance.title("上傳心率"))
                        val resp = apiRepository.uploadHr(
                            UploadHrRequest(
                                memberId,
                                startTime,
                                item.heartValue
                            )
                        )

                        if (!resp.code.equals("0x0200"))
                            uploadStatus = false
                        else {
                            if (item.heartStartTime > newlastTime)
                                newlastTime = item.heartStartTime
                        }
                    }
                }
                if (newlastTime > lastTime) {
                    SharedPreferencesUtil.instances.setWatchLongValue(
                        SharedPreferencesUtil.instances.KEY_LAST_UPLOADD_HR_TIME,
                        newlastTime
                    )
                }
                return uploadStatus
            }

            Constants.DATATYPE.Health_HistoryBlood -> {
                val list = data as List<HistoryBloodBPInfo>
                val lastTime = SharedPreferencesUtil.instances.getWatchLongValue(
                    SharedPreferencesUtil.instances.KEY_LAST_UPLOADD_BP_TIME
                )
                var newlastTime = 0L
                var uploadStatus = true
                for (item in list) {
                    if (item.bloodStartTime > lastTime) {
                        val startTime = sdf.format(Date(item.bloodStartTime))

                        Log.v(TAG, AppUtil.instance.title("上傳血壓"))
                        val resp = apiRepository.uploadBp(
                            UploadBPRequest(
                                memberId,
                                startTime,
                                item.bloodDBP,
                                item.bloodSBP
                            )
                        )

                        if (!resp.code.equals("0x0200"))
                            uploadStatus = false
                        else {
                            if (item.bloodStartTime > newlastTime)
                                newlastTime = item.bloodStartTime
                        }
                    }
                }
                if (newlastTime > lastTime) {
                    SharedPreferencesUtil.instances.setWatchLongValue(
                        SharedPreferencesUtil.instances.KEY_LAST_UPLOADD_BP_TIME,
                        newlastTime
                    )
                }

                return uploadStatus
            }

            Constants.DATATYPE.Health_HistoryAll -> {
                val list = data as List<HistoryHealthInfo>

//                uploadOxygen(list)
                val oxygenlist: MutableList<HistoryOxygenInfo> = ArrayList()
                val temperatureList: MutableList<HistoryTemperatureInfo> = ArrayList()
                val respiratoryRateList: MutableList<HistoryRespiratoryRateInfo> = ArrayList()

                for (d in list) {
                    val oxy = HistoryOxygenInfo(d.startTime, d.oOValue)
                    oxygenlist.add(oxy)

                    val temperature =
                        HistoryTemperatureInfo(d.startTime, d.tempIntValue, d.tempFloatValue)
                    temperatureList.add(temperature)

                    val respiratoryRate =
                        HistoryRespiratoryRateInfo(d.startTime, d.respiratoryRateValue)
                    respiratoryRateList.add(respiratoryRate)
                }

                val oxygenLastTime = SharedPreferencesUtil.instances.getWatchLongValue(
                    SharedPreferencesUtil.instances.KEY_LAST_UPLOADD_OXYGEN_TIME
                )
                val temperatureLastTime = SharedPreferencesUtil.instances.getWatchLongValue(
                    SharedPreferencesUtil.instances.KEY_LAST_UPLOADD_TEMPERATURE_TIME
                )
                val respiratoryRateLastTime = SharedPreferencesUtil.instances.getWatchLongValue(
                    SharedPreferencesUtil.instances.KEY_LAST_UPLOADD_RESPIRATORY_RATE_TIME
                )
                var newOxygenLastTime = 0L
                var newTemperatureLastTime = 0L
                var newRespiratoryRateLastTime = 0L
                var uploadStatus = true

                for (item in oxygenlist) {
                    if (item.startTime > oxygenLastTime && item.ooValue > 0) {
                        val starttime = sdf.format(Date(item.startTime))

                        Log.v(TAG, AppUtil.instance.title("上傳血氧"))
                        val resp = apiRepository.uploadOxygen(
                            UploadOxygenRequest(
                                memberId,
                                starttime,
                                item.ooValue
                            )
                        )

                        if (!resp.code.equals("0x0200")) {
                            uploadStatus = false
                        } else {
                            if (item.startTime > newOxygenLastTime)
                                newOxygenLastTime = item.startTime
                        }
                    }
                }

                for (item in temperatureList) {
                    val fullTemperature = "${item.temperatureValue}.${item.tempFloatValue}"
                    if (item.startTime > temperatureLastTime) {
                        val startTime = sdf.format(Date(item.startTime))
                        var temperature = 0f
                        runCatching {
                            temperature = fullTemperature.toFloat()
                        }

                        if (RangeBodyTem.Mini.value < temperature &&
                            temperature < RangeBodyTem.Max.value
                        ) {

                            Log.v(TAG, AppUtil.instance.title("上傳體溫"))
                            val resp = apiRepository.uploadTemperature(
                                UploadTemperatureRequest(
                                    memberId,
                                    startTime,
                                    fullTemperature.toFloat()
                                )
                            )

                            if (!resp.code.equals("0x0200")) {
                                uploadStatus = false
                            } else {
                                if (item.startTime > newTemperatureLastTime)
                                    newTemperatureLastTime = item.startTime
                            }
                        }
                    }
                }

                for (item in respiratoryRateList) {
                    if (item.startTime > respiratoryRateLastTime && item.respiratoryRateValue > 0) {
                        val startTime = sdf.format(Date(item.startTime))

                        Log.v(TAG, AppUtil.instance.title("上傳呼吸率"))
                        val resp = apiRepository.uploadRespiratoryrate(
                            UploadRespiratoryRateRequest(
                                memberId,
                                startTime,
                                item.respiratoryRateValue
                            )
                        )

                        if (!resp.code.equals("0x0200")) {
                            uploadStatus = false
                        } else {
                            if (item.startTime > newRespiratoryRateLastTime)
                                newRespiratoryRateLastTime = item.startTime
                        }
                    }
                }

                if (newOxygenLastTime > oxygenLastTime) {
                    SharedPreferencesUtil.instances.setWatchLongValue(
                        SharedPreferencesUtil.instances.KEY_LAST_UPLOADD_OXYGEN_TIME,
                        newOxygenLastTime
                    )
                }

                if (newTemperatureLastTime > temperatureLastTime) {
                    SharedPreferencesUtil.instances.setWatchLongValue(
                        SharedPreferencesUtil.instances.KEY_LAST_UPLOADD_TEMPERATURE_TIME,
                        newTemperatureLastTime
                    )
                }

                if (newRespiratoryRateLastTime > respiratoryRateLastTime) {
                    SharedPreferencesUtil.instances.setWatchLongValue(
                        SharedPreferencesUtil.instances.KEY_LAST_UPLOADD_RESPIRATORY_RATE_TIME,
                        newRespiratoryRateLastTime
                    )
                }

                return uploadStatus
            }

            Constants.DATATYPE.Health_HistorySport -> {
                val list = data as List<HistorySportInfo>
                val lastTime = SharedPreferencesUtil.instances.getWatchLongValue(
                    SharedPreferencesUtil.instances.KEY_LAST_UPLOADD_STEP_TIME
                )
                var newlastTime = 0L
                var uploadStatus = true
                for (item in list) {
                    if (item.sportStartTime > lastTime) {
                        val starttime = sdf.format(Date(item.sportStartTime))
                        val endtime = sdf.format(Date(item.sportEndTime))

                        Log.v(TAG, AppUtil.instance.title("上傳步數"))
                        val resp = apiRepository.uploadSteps(
                            UploadStepRequest(
                                memberId,
                                starttime,
                                endtime,
                                item.sportStep,
                                item.sportCalorie,
                                item.sportDistance
                            )
                        )

                        if (!resp.code.equals("0x0200")) {
                            uploadStatus = false
                        } else {
                            if (item.sportStartTime > newlastTime)
                                newlastTime = item.sportStartTime
                        }
                    }
                }
                if (newlastTime > lastTime) {
                    SharedPreferencesUtil.instances.setWatchLongValue(
                        SharedPreferencesUtil.instances.KEY_LAST_UPLOADD_STEP_TIME,
                        newlastTime
                    )
                }

                return uploadStatus
            }
        }
        return false
    }

    suspend fun asyncHealthHistoryData(type: Int) {
        Log.w(TAG, "asyncHealthHistoryData type: $type")
        when (type) {
            Constants.DATATYPE.Health_HistoryHeart -> {
                var datalist: List<HistoryHeartInfo>? = mutableListOf()
                Log.w(TAG, "474 doneSignal: $doneSignal")
                doneSignal = CountDownLatch(1)
                getHealthHistoryData(type) { code, status, hashMap ->
                    Log.w(TAG, "code: $code")
                    if (code == Constants.CODE.Code_OK) {
                        Log.w(TAG, "hashMap: $hashMap")
                        if (hashMap != null) {
                            val response: YCBTDataResponse? =
                                YCBTDataHelper.YCBTDataResponseFromMap(hashMap as HashMap<String?, Any?>?)
                            Log.w(TAG, "response: ${response}")
                            if (response != null) {
                                Log.w(TAG, "response.data: ${response.data}")
                                datalist =
                                    YCBTDataHelper.historyHeartInfosListFromObject(response.data)
                                Log.w(TAG, "datalist: $datalist")
                            }
                        }
                    }
                    Log.w(TAG, "488 doneSignal: $doneSignal")
                    doneSignal?.countDown()
                }
                Log.w(TAG, "asyncHealthHistoryData: 491")
                doneSignal!!.await()
                Log.w(TAG, "asyncHealthHistoryData: 493")
                if (uploadHealthHistoryData(type, datalist as List<Any>))
                    delHealthHistoryData(0x542)
            }
            Constants.DATATYPE.Health_HistoryAll -> {
                var datalist: List<HistoryHealthInfo>? = mutableListOf()
                doneSignal = CountDownLatch(1)
                getHealthHistoryData(type) { code, status, hashMap ->
                    if (code == Constants.CODE.Code_OK) {
                        if (hashMap != null) {
                            val response: YCBTDataResponse? =
                                YCBTDataHelper.YCBTDataResponseFromMap(hashMap as HashMap<String?, Any?>?)
                            Log.d(TAG, "asyncHealthHistoryData response: $response")
                            if (response != null) {
                                datalist =
                                    YCBTDataHelper.historyHealthInfosListFromObject(response.data)
                            }
                        }
                    }
                    doneSignal?.countDown()
                }
                doneSignal!!.await()
                if (uploadHealthHistoryData(type, datalist as List<Any>))
                    delHealthHistoryData(0x544)
            }
            Constants.DATATYPE.Health_HistoryBlood -> {
                var datalist: List<HistoryBloodBPInfo>? = mutableListOf()
                doneSignal = CountDownLatch(1)
                getHealthHistoryData(type) { code, status, hashMap ->
                    if (code == Constants.CODE.Code_OK) {
                        if (hashMap != null) {
                            val response: YCBTDataResponse? =
                                YCBTDataHelper.YCBTDataResponseFromMap(hashMap as HashMap<String?, Any?>?)
                            if (response != null) {
                                datalist =
                                    YCBTDataHelper.historyBloodInfosListFromObject(response.data)
                            }
                        }
                    }
                    doneSignal?.countDown()
                }
                doneSignal!!.await()
                if (uploadHealthHistoryData(type, datalist as List<Any>))
                    delHealthHistoryData(0x543)
            }
            Constants.DATATYPE.Health_HistorySport -> {
                var datalist: List<HistorySportInfo>? = mutableListOf()
                doneSignal = CountDownLatch(1)
                getHealthHistoryData(type) { code, status, hashMap ->
                    if (code == Constants.CODE.Code_OK) {
                        if (hashMap != null) {
                            val response: YCBTDataResponse? =
                                YCBTDataHelper.YCBTDataResponseFromMap(hashMap as HashMap<String?, Any?>?)
                            if (response != null) {
                                datalist =
                                    YCBTDataHelper.historySportInfoListFromObject(response.data)
                            }
                        }
                    }
                    doneSignal?.countDown()
                }
                doneSignal!!.await()
                if (uploadHealthHistoryData(type, datalist as List<Any>))
                    delHealthHistoryData(0x540)
            }
            Constants.DATATYPE.Health_HistorySleep -> {
                var datalist: List<HistorySleepInfo>? = mutableListOf()
                doneSignal = CountDownLatch(1)
                getHealthHistoryData(type) { code, status, hashMap ->
                    if (code == Constants.CODE.Code_OK) {
                        if (hashMap != null) {
                            val response: YCBTDataResponse? =
                                YCBTDataHelper.YCBTDataResponseFromMap(hashMap as HashMap<String?, Any?>?)
                            if (response != null) {
                                datalist =
                                    YCBTDataHelper.historySleepInfoListFromObject(response.data)
                            }
                        }
                    }
                    doneSignal?.countDown()
                }
                doneSignal!!.await()
                if (uploadHealthHistoryData(type, datalist as List<Any>))
                    delHealthHistoryData(0x541)
            }
            Constants.DATATYPE.AppTodayWeather -> {

                val adminArea = SharedPreferencesUtil.instances.getAdminArea()
                if (adminArea.isNullOrBlank()) {
                    Log.d(TAG, "adminArea = null")
                    return
                }
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                val calendar = Calendar.getInstance()
                val timeForm = sdf.format(calendar.time).replace(' ', 'T')

                calendar.add(Calendar.DATE, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 23)
                val timeTo = sdf.format(calendar.time).replace(' ', 'T')
                val tomorrow = timeTo.substring(0, timeTo.indexOf("T"))

                ApiConnect().getWeather(
                    adminArea, timeForm, timeTo, object : ApiConnect.resultListener {
                        override fun onSuccess(message: String) {

                            var todayT = ""
                            var tomorrowT = ""
                            var todayMinT = ""
                            var tomorrowMinT = ""
                            var todayMaxT = ""
                            var tomorrowMaxT = ""
                            var todayWx = 0
                            var tomorrowWx = 0

                            try {
                                val array = JSONArray(message)
                                var tomorrowIndex = 0
                                val timeArray = array.getJSONObject(0).getJSONArray("time")
                                for (i in 1 until timeArray.length()) {
                                    if (timeArray.getJSONObject(i).getString("startTime")
                                            .contains(tomorrow)
                                    ) {
                                        tomorrowIndex = i
                                        break
                                    }
                                }
                                if (tomorrowIndex == 0) {
                                    Log.d(TAG, "沒有明天的時間")
                                    return
                                }

                                for (i in 0 until array.length()) {
                                    when (array.getJSONObject(i).getString("elementName")) {
                                        "T" -> {
                                            todayT = array.getJSONObject(i)
                                                .getJSONArray("time").getJSONObject(0)
                                                .getJSONArray("elementValue").getJSONObject(0)
                                                .getString("value")

                                            tomorrowT = array.getJSONObject(i)
                                                .getJSONArray("time").getJSONObject(tomorrowIndex)
                                                .getJSONArray("elementValue").getJSONObject(0)
                                                .getString("value")
                                            Log.d(TAG, "todayT: $todayT - tomorrowT: $tomorrowT")
                                        }

                                        "MinT" -> {
                                            todayMinT = array.getJSONObject(i)
                                                .getJSONArray("time").getJSONObject(0)
                                                .getJSONArray("elementValue").getJSONObject(0)
                                                .getString("value")

                                            tomorrowMinT = array.getJSONObject(i)
                                                .getJSONArray("time").getJSONObject(tomorrowIndex)
                                                .getJSONArray("elementValue").getJSONObject(0)
                                                .getString("value")
                                            Log.d(
                                                TAG,
                                                "todayMinT: $todayMinT - tomorrowMinT: $tomorrowMinT"
                                            )
                                        }

                                        "MaxT" -> {
                                            todayMaxT = array.getJSONObject(i)
                                                .getJSONArray("time").getJSONObject(0)
                                                .getJSONArray("elementValue").getJSONObject(0)
                                                .getString("value")

                                            tomorrowMaxT = array.getJSONObject(i)
                                                .getJSONArray("time").getJSONObject(tomorrowIndex)
                                                .getJSONArray("elementValue").getJSONObject(0)
                                                .getString("value")
                                            Log.d(
                                                TAG,
                                                "todayMaxT: $todayMaxT - tomorrowMaxT: $tomorrowMaxT"
                                            )
                                        }

                                        "Wx" -> {
                                            todayWx = array.getJSONObject(i)
                                                .getJSONArray("time").getJSONObject(0)
                                                .getJSONArray("elementValue").getJSONObject(1)
                                                .getString("value").toInt()
                                            todayWx = processWxValue(todayWx)

                                            tomorrowWx = array.getJSONObject(i)
                                                .getJSONArray("time").getJSONObject(tomorrowIndex)
                                                .getJSONArray("elementValue").getJSONObject(1)
                                                .getString("value").toInt()
                                            tomorrowWx = processWxValue(tomorrowWx)
                                            Log.d(
                                                TAG,
                                                "todayWx: $todayWx - tomorrowWx: $tomorrowWx"
                                            )
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                return
                            }

                            if (todayT.isNullOrEmpty() ||
                                todayMinT.isNullOrEmpty() ||
                                todayMaxT.isNullOrEmpty()
                            ) {
                                Log.d(TAG, "今日氣象資料有空值")
                                return
                            }

                            doneSignal = CountDownLatch(1)
                            YCBTClient.appTodayWeather(
                                todayMinT,
                                todayMaxT,
                                todayT,
                                todayWx
                            ) { i: Int, fl: Float, hashMap: HashMap<Any, Any> ->
                                Log.w(TAG, "今日氣象藍牙回傳: $i - $fl - $hashMap")
                            }
                            doneSignal?.countDown()
                            doneSignal!!.await()

                            if (tomorrowT.isNullOrEmpty() ||
                                tomorrowMinT.isNullOrEmpty() ||
                                tomorrowMaxT.isNullOrEmpty()
                            ) {
                                Log.d(TAG, "明日氣象資料有空值")
                                return
                            }
                            doneSignal = CountDownLatch(1)
                            YCBTClient.appTomorrowWeather(
                                tomorrowMinT,
                                tomorrowMaxT,
                                tomorrowT,
                                tomorrowWx
                            ) { i: Int, fl: Float, hashMap: HashMap<Any, Any> ->
                                Log.w(TAG, "明日氣象藍牙回傳: $i - $fl - $hashMap")
                            }
                            doneSignal?.countDown()
                            doneSignal!!.await()


                        }

                        override fun onFailure(task: String?, message: String?) {
                            Log.d(TAG, "氣象API onFailure")
                        }

                    }
                )
            }
        }
    }

    fun processWxValue(value: Int): Int {
        val num: Int
        when (value) {
            in 0..6 -> {
                // 晴
                num = 1
                Log.d(TAG, "晴")
            }
            in 7..10 -> {
                // 多雲
                num = 2
                Log.d(TAG, "多雲")
            }
            in 11..22 -> {
                // 雨
                num = 3
                Log.d(TAG, "雨")
            }
            in 29..41 -> {
                // 雨
                num = 3
                Log.d(TAG, "雨")
            }
            23 -> {
                // 雪
                num = 4
                Log.d(TAG, "雪")
            }
            42 -> {
                // 雪
                num = 4
                Log.d(TAG, "雪")
            }
            in 24..28 -> {
                // 霧
                num = 6
                Log.d(TAG, "霧")
            }
            else -> {
                // 未知
                num = 0
                Log.d(TAG, "未知")
            }
        }
        return num
    }

    fun delHealthHistoryData(type: Int) {
        Log.w(TAG, "delHealthHistoryData type: $type")
        doneSignal = CountDownLatch(1)
        Log.w(TAG, "doneSignal: $doneSignal")
        YCBTClient.deleteHealthHistoryData(
            type
        ) { code, status, hashMap ->
            Log.w(TAG, "delHealthHistoryData code: $code")
            if (code == Constants.CODE.Code_OK) {
                Log.w(TAG, "type: $type  Code_OK")
            }
            Log.w(TAG, "810 doneSignal: $doneSignal")
            doneSignal?.countDown()
        }
        Log.w(TAG, "813 doneSignal: $doneSignal")
        doneSignal!!.await()
    }

    fun initWatchWorker() {
        val uploadWorkRequest: WorkRequest =
            OneTimeWorkRequest.Builder(WatchWorker::class.java)
                //.setInputData(
                //    Data.Builder()
                //        .putBoolean(SmartWatchConstants.KEY_WORKER_REPEAT, repeat)
                //        .putLong(SmartWatchConstants.KEY_WORKER_REPEAT_DURATION, 2)
                //        .build()
                //)
                .addTag(KEY_SYNC_WATCH_WORKER_NAME)
                //  .setInitialDelay(1, TimeUnit.SECONDS)
                .build()
        WorkManager
            .getInstance(appContext)
            .enqueue(uploadWorkRequest)
    }

    fun cancelAllWorker() {
        WorkManager
            .getInstance(appContext)
            .cancelAllWorkByTag(KEY_SYNC_WATCH_WORKER_NAME)
    }


    fun fixDecimalZero(value: Float): String {
        return DecimalFormat("#").format(value)
    }

    fun fixDecimalOne(value: Float): String {
        return DecimalFormat("#.0").format(value)
    }


    fun inputType(type: String): String {
        return when (type) {
            "0" -> "手錶數值"
            "1" -> "手動輸入數據"
            else -> "醫事人員輸入數據"
        }
    }

    fun initOneTimeWorker(delay: Long, unit: TimeUnit) {
        val uploadWorkRequest: WorkRequest =
            OneTimeWorkRequest.Builder(SmartWatchWalkDataWorker::class.java)
                //.setInputData(
                //    Data.Builder()
                //        .putBoolean(SmartWatchConstants.KEY_WORKER_REPEAT, repeat)
                //        .putLong(SmartWatchConstants.KEY_WORKER_REPEAT_DURATION, 2)
                //        .build()
                //)
                .addTag(KEY_SYNC_WATCH_WORKER_NAME)
                .setInitialDelay(delay, unit)
                .build()
        WorkManager
            .getInstance(appContext)
            .enqueue(uploadWorkRequest)

    }

    // ECG
    fun collectEcgList(dataResponse: BleDataResponse) {
        YCBTClient.collectEcgList(dataResponse)
    }

    // ECG 手錶資料清單
    fun collectHistoryListData(type: Int, dataResponse: BleDataResponse) {
        YCBTClient.collectHistoryListData(type, dataResponse)
    }

    // 綜合命令 - 取各種資料
    fun collectHistoryDataWithTimestamp(type: Int, timestamp: Long, dataResponse: BleDataResponse) {
        YCBTClient.collectHistoryDataWithTimestamp(type, timestamp, dataResponse)
    }

    // ECG 指定時間的該檔案上傳資料到 App
    fun collectEcgDataWithTimestamp(sendTime: Long, dataResponse: BleDataResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            YCBTClient.collectEcgDataWithTimestamp(sendTime, dataResponse)
        }
    }

    // 綜合命令 - 刪除歷史數據
    fun deleteHistoryListData(type: Int, sendTime: Long, dataResponse: BleDataResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            YCBTClient.deleteHistoryListData(type, sendTime, dataResponse)
        }
    }

    // 心率監測頻率
    fun settingHeartMonitor(mode: Int, intervalTime: Int, dataResponse: BleDataResponse) {
        YCBTClient.settingHeartMonitor(mode, intervalTime, dataResponse)
    }

    // 體溫監測頻率
    fun settingTemperatureMonitor(on_off: Boolean, interval: Int, dataResponse: BleDataResponse) {
        YCBTClient.settingTemperatureMonitor(on_off, interval, dataResponse)
    }

    // 血氧監測頻率
    fun settingBloodOxygenModeMonitor(
        on_off: Boolean,
        interval: Int,
        dataResponse: BleDataResponse
    ) {
        YCBTClient.settingBloodOxygenModeMonitor(on_off, interval, dataResponse)
    }

    // 註冊 藍牙狀態改變 監聽
    fun registerBleStateChange(isConnectAction: (Boolean) -> Unit) {

        YCBTClient.registerBleStateChange {

            when (it) {
                Constants.BLEState.Disconnect -> {
                    Log.d(TAG, "藍牙斷線")
                    isConnectAction(false)
                }
                Constants.BLEState.Connected -> {
                    Log.d(TAG, "藍牙連線")
                    isConnectAction(true)
                }
                Constants.BLEState.ReadWriteOK -> Log.d(TAG, "讀寫OK: ")
            }
        }
    }

    // 解除 藍牙狀態改變 監聽
    fun unRegisterBleStateChange() {
        YCBTClient.unRegisterBleStateChange {
            Log.d(TAG, "unRegisterBleStateChange: $it")
        }
    }
}