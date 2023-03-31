package com.jotangi.NumberHealthy.utils.smartwatch.model

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

class HistorySleepDetailInfo {
    @SerializedName(KEY_SLEEP_TYPE)
    var sleepType // -- 0xF1:深睡 0xF2:浅睡
            = 0

    @SerializedName(KEY_SLEEP_START_TIME)
    var sleepStartTime // -- 开始时间戳
            : String = ""

    @SerializedName(KEY_SLEEP_LEN)
    var sleepLen // -- 睡眠时长 单位秒
            = 0

    constructor() {}
    constructor(sleepType: Int, sleepStartTime: Long, sleepLen: Int) {
        this.sleepType = sleepType
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val starttime = sdf.format(Date(sleepStartTime))
        this.sleepStartTime = starttime
        this.sleepLen = sleepLen
    }

    companion object {
        const val KEY_SLEEP_TYPE = "sleepType"
        const val KEY_SLEEP_START_TIME = "sleepStartTime"
        const val KEY_SLEEP_LEN = "sleepLen"
    }
}