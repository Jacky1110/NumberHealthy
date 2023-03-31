package com.jotangi.NumberHealthy.api.watch

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Common Response
 */
@Parcelize
open class WatchDataResponse(
    var status: String? = null,
    var code: String? = null,
    var responseMessage: String? = null
) : Parcelable

/**
 * 07. App 取得步數資料
 */
@Parcelize
data class GetStepsData(
    var data: List<GetStepsBean>? = listOf()
) : Parcelable, WatchDataResponse()

@Parcelize
data class GetStepsBean(
    var sportStartTime: String? = null,
    var sportEndTime: String? = null,
    var sportStep: String? = null,
    var sportCalorie: String? = null,
    var sportDistance: String? = null
) : Parcelable

/**
 * 08. App 取得睡眠數據
 */
@Parcelize
data class GetSleepResponse(
    var data: List<GetSleepBean>? = listOf()
) : Parcelable, WatchDataResponse()

@Parcelize
data class GetSleepBean(
    var startTime: String? = null,
    var endTime: String? = null,
    var deepSleepCount: String? = null,
    var lightSleepCount: String? = null,
    var deepSleepTotal: String? = null,
    var lightSleepTotal: String? = null
) : Parcelable

@Parcelize
data class SleepDayTotalData(
    var startTime: String? = null,
    var endTime: String? = null,
    var deepMinuteTotal: Int = 0,
    var lightMinuteTotal: Int = 0,
) : Parcelable

/**
 * 09. App 取得睡眠細節
 */
@Parcelize
data class SleepDataResponse(
    var data: List<SleepDataBean>? = listOf()
) : Parcelable, WatchDataResponse()

@Parcelize
data class SleepDataBean(
    var sleepStartTime: String? = null,
    var sleepType: String? = null,
    var sleepLen: String? = null
) : Parcelable

@Parcelize
data class SleepLineData(
    var deepMinuteList: List<Int> = listOf(),
    var totalMinuteList: List<Int> = listOf(),
) : Parcelable

/**
 * 10. App 取得心率數值
 */
@Parcelize
data class HeartRateResponse(
    var data: List<HeartRateBean>? = listOf()
) : Parcelable, WatchDataResponse()

@Parcelize
data class HeartRateBean(
    var heartStartTime: String? = null,
    var heartValue: String? = null,
    var dataType: String? = null
) : Parcelable

/**
 * 11. App 取得血壓數值
 */
@Parcelize
data class BPResponse(
    var data: List<BPBean>? = listOf()
) : Parcelable, WatchDataResponse()

@Parcelize
data class BPBean(
    var bloodStartTime: String? = null,
    var bloodDBP: String? = null,
    var bloodSBP: String? = null,
    var dataType: String? = null
) : Parcelable

/**
 * 13. App 取得血氧數值
 */
@Parcelize
data class OxygenResponse(
    var data: List<OxygenBean>? = listOf()
) : Parcelable, WatchDataResponse()

@Parcelize
data class OxygenBean(
    var startTime: String? = null,
    var OOValue: String? = null,
    var dataType: String? = null
) : Parcelable

/**
 * 15. App 取得ECG數值
 */
@Parcelize
data class EcgResponse(
    var data: List<EcgBean>? = listOf()
) : Parcelable, WatchDataResponse()

@Parcelize
data class EcgBean(
    var ecgStartTime: String? = null,
    var ecgValue: String? = null,
    var hr: Int = 0,
    var dbp: Int = 0,
    var sbp: Int = 0,
    var hrv: Int = 0
) : Parcelable

/**
 * 17. App 取得呼吸率數值
 */
@Parcelize
data class BreathRateResponse(
    var data: List<BreathRateBean>? = listOf()
) : Parcelable, WatchDataResponse()

@Parcelize
data class BreathRateBean(
    var startTime: String? = null,
    var respiratoryrate: String? = null,
    var dataType: String? = null
) : Parcelable

/**
 * 19. App 取得體溫數值
 */
@Parcelize
data class TemperatureResponse(
    var data: List<TemperatureBean>? = listOf()
) : Parcelable, WatchDataResponse()

@Parcelize
data class TemperatureBean(
    var startTime: String? = null,
    var temperature: String = "",
    var dataType: String? = null
) : Parcelable

/**
 * 20. App 取得會員手環的保固資料
 */
@Parcelize
data class GetWarrantyInfoResponse(
    var data: List<GetWarrantyInfoBean>? = arrayListOf()
) : Parcelable, WatchDataResponse()

@Parcelize
data class GetWarrantyInfoBean(
    var startTime: String? = null,
    var endTime: String? = null,
) : Parcelable

/**
 * 22.App 取得骨密的檢查數值
 */
@Parcelize
data class GetBmdData(
    var data: List<GetBmdBean>? = arrayListOf()
) : Parcelable, WatchDataResponse()

@Parcelize
data class GetBmdBean(
    var startTime: String? = null,
    var TScore: String? = null,
    var dataType: String? = null
) : Parcelable

/**
 * 24.App 取得黃斑部色素檢查數值
 */
@Parcelize
data class GetMpodData(
    var data: List<GetMpodBean>? = arrayListOf()
) : Parcelable, WatchDataResponse()

@Parcelize
data class GetMpodBean(
    var mpodStartTime: String? = null,
    var lefteye: String? = null,
    var righteye: String? = null,
    var dataType: String? = null
) : Parcelable

/**
 * 26.App 取得雙臂血壓數值
 */
@Parcelize
data class GetBp2Data(
    var data: List<GetBp2Bean>? = arrayListOf()
) : Parcelable, WatchDataResponse()

@Parcelize
data class GetBp2Bean(
    var bloodStartTime: String? = null,
    var LbloodDBP: String? = null,
    var LbloodSBP: String? = null,
    var LbloodPP: String? = null,
    var LbloodMAP: String? = null,
    var RbloodDBP: String? = null,
    var RbloodSBP: String? = null,
    var RbloodPP: String? = null,
    var RbloodMAP: String? = null,
    var heartValue: String? = null,
    var dataType: String? = null,
) : Parcelable

/**
 * 28. App 取得卡路里的數值
 */
@Parcelize
data class GetKcalData(
    var data: List<GetKcalDataBean>? = arrayListOf()
) : Parcelable, WatchDataResponse()

@Parcelize
data class GetKcalDataBean(
    var startTime: String? = null,
    var KCAL: String? = null,
    var dataType: String? = null,
) : Parcelable

@Parcelize
data class Kcal7Data(
    var date: String = "",
    var kcal: String = "",
) : Parcelable