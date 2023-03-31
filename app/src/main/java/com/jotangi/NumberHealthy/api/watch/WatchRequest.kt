package com.jotangi.NumberHealthy.api.watch

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * Common Request
 */
@Parcelize
open class ElementRequest(
    var memberId: String = "",
    var startTime: String = "",
    var endTime: String = ""
) : Parcelable


/**
 * 01. App 上傳會員手環的步數資料
 */
@Parcelize
data class UploadStepRequest(
    var memberId: String = "",
    var sportStartTime: String = "",
    var sportEndTime: String = "",
    var sportStep: Int = 0,
    var sportCalorie: Int = 0,
    var sportDistance: Int = 0
) : Parcelable

/**
 * 02. App 上傳會員手環的睡眠資料
 */
@Parcelize
data class UploadSleepRequest(
    var memberId: String = "",
    var startTime: String = "",
    var endTime: String = "",
    var deepSleepCount: Int = 0,
    var lightSleepCount: Int = 0,
    var deepSleepTotal: Int = 0,
    var lightSleepTotal: Int = 0,
    var sleepData: String? = ""
) : Parcelable

/**
 * 03. App 上傳會員手環的心率資料
 */
@Parcelize
data class UploadHrRequest(
    var memberId: String = "",
    var heartStartTime: String = "",
    var heartValue: Int = 0,
    var dataType: Int = 0
) : Parcelable

/**
 * 04. App 上傳會員手環的血壓資料
 */
@Parcelize
data class UploadBPRequest(
    var memberId: String = "",
    var bloodStartTime: String = "",
    var bloodDBP: Int = 0,
    var bloodSBP: Int = 0,
    var dataType: Int = 0
) : Parcelable

/**
 * 05. App 上傳會員手環的血氧數值
 */
@Parcelize
data class UploadOxygenRequest(
    var memberId: String = "",
    var startTime: String = "",
    var OOValue: Int = 0,
    var dataType: Int = 0
) : Parcelable

/**
 * 14. App 上傳會員手環的ECG資料
 */
@Parcelize
data class UploadEcgRequest(
    var memberId: String = "",
    var ecgStartTime: String = "",
    var ecgValue: String = "",
    var hr: Int = 0,
    var dbp: Int = 0,
    var sbp: Int = 0,
    var hrv: Int = 0
) : Parcelable

/**
 * 16. App 上傳會員手環的呼吸率資料
 */
@Parcelize
data class UploadRespiratoryRateRequest(
    var memberId: String = "",
    var startTime: String = "",
    var respiratoryrate: Int = 0,
    var dataType: Int = 0
) : Parcelable

/**
 * 18. App 上傳會員手環的體溫資料
 */
@Parcelize
data class UploadTemperatureRequest(
    var memberId: String = "",
    var startTime: String = "",
    var temperature: Float = 0f,
    var dataType: Int = 0
) : Parcelable

/**
 * 21. App 上傳會員骨密的檢查數值
 */
@Parcelize
data class UploadBmdRequest(
    var memberId: String = "",
    var startTime: String = "",
    var TScore: String = "",
    var dataType: String = ""
) : Parcelable

/**
 * 23. App 上傳會員黃斑部色素檢查數值
 */
@Parcelize
data class UploadMpodRequest(
    var memberId: String = "",
    var mpodStartTime: String = "",
    var lefteye: String = "",
    var righteye: String = "",
    var dataType: String = ""
) : Parcelable

/**
 * 25. App 上傳會員雙臂的血壓資料
 */
@Parcelize
data class UploadArmRequest(
    var memberId: String = "",
    var bloodStartTime: String = "",
    var LbloodDBP: String = "",
    var LbloodSBP: String = "",
    var LbloodPP: String = "",
    var LbloodMAP: String = "",
    var RbloodDBP: String = "",
    var RbloodSBP: String = "",
    var RbloodPP:  String = "",
    var RbloodMAP: String = "",
    var heartValue: String = "",
    var dataType: String = "",
) : Parcelable

/**
 * 27. App 上傳會員卡路里的數值
 */
@Parcelize
data class UploadKcalRequest(
    var memberId: String = "",
    var startTime: String = "",
    var KCAL: String = "",
    var dataType: String = "",
) : Parcelable
