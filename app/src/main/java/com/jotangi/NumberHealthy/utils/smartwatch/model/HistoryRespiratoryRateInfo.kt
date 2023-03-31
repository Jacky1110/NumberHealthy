package com.jotangi.NumberHealthy.utils.smartwatch.model

import com.google.gson.annotations.SerializedName

class HistoryRespiratoryRateInfo {
    @SerializedName(KEY_START_TIME)
    var startTime // -- 测试时间
            : Long = 0

    @SerializedName(KEY_RESPIRATORY_RATE_VALUE)
    var respiratoryRateValue // -- 呼吸率
            = 0

    constructor(
        startTime: Long,
        respiratoryRateValue: Int,
    ) {
        this.startTime = startTime
        this.respiratoryRateValue = respiratoryRateValue
    }

    companion object {
        const val KEY_START_TIME = "startTime"
        const val KEY_RESPIRATORY_RATE_VALUE = "respiratoryRateValue"
    }
}