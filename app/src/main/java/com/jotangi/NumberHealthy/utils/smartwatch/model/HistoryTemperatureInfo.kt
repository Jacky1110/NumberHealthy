package com.jotangi.NumberHealthy.utils.smartwatch.model

import com.google.gson.annotations.SerializedName

class HistoryTemperatureInfo {
    @SerializedName(KEY_START_TIME)
    var startTime // 體溫测试时间
            : Long = 0

    @SerializedName(KEY_TEMPERATURE_VALUE)
    var temperatureValue // 溫度
            = 0

    @SerializedName(KEY_TEMPFLOAT_VALUE)
    var tempFloatValue // 溫度小數點
            = 0

    constructor(startTime: Long, temperatureValue: Int, tempFloatValue: Int) {
        this.startTime = startTime
        this.temperatureValue = temperatureValue
        this.tempFloatValue = tempFloatValue
    }

    companion object {
        const val KEY_START_TIME = "StartTime"
        const val KEY_TEMPERATURE_VALUE = "TemperatureValue"
        const val KEY_TEMPFLOAT_VALUE = "TempFloatValue"
    }
}