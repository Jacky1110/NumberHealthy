package com.jotangi.NumberHealthy.utils.smartwatch.model

import com.google.gson.annotations.SerializedName

class HistoryHealthInfo {
    @SerializedName(KEY_START_TIME)
    var startTime // -- 测试时间
            : Long = 0

    @SerializedName(KEY_STEP_VALUE)
    var stepValue // -- 步数
            = 0

    @SerializedName(KEY_HEART_VALUE)
    var heartValue // -- 心率值
            = 0

    @SerializedName(KEY_DBP_VALUE)
    var dBPValue // -- 收缩压
            = 0

    @SerializedName(KEY_SBP_VALUE)
    var sBPValue // -- 舒张压
            = 0

    @SerializedName(KEY_OOV_VALUE)
    var oOValue // -- 血氧
            = 0

    @SerializedName(KEY_RESPIRATORY_RATE_VALUE)
    var respiratoryRateValue // -- 呼吸率
            = 0

    @SerializedName(KEY_HRV_VALUE)
    var hrvValue // -- hrv
            = 0

    @SerializedName(KEY_CVRR_VALUE)
    var cvrrValue // -- cvrr t
            = 0

    @SerializedName(KEY_TEMP_INT_VALUE)
    var tempIntValue // -- 温度整数部分
            = 0

    @SerializedName(KEY_TEMP_FLOAT_VALUE)
    var tempFloatValue // -- 温度小数部分
            = 0

    constructor() {}
    constructor(
        startTime: Long,
        stepValue: Int,
        heartValue: Int,
        DBPValue: Int,
        SBPValue: Int,
        OOValue: Int,
        respiratoryRateValue: Int,
        hrvValue: Int,
        cvrrValue: Int,
        tempIntValue: Int,
        tempFloatValue: Int
    ) {
        this.startTime = startTime
        this.stepValue = stepValue
        this.heartValue = heartValue
        dBPValue = DBPValue
        sBPValue = SBPValue
        oOValue = OOValue
        this.respiratoryRateValue = respiratoryRateValue
        this.hrvValue = hrvValue
        this.cvrrValue = cvrrValue
        this.tempIntValue = tempIntValue
        this.tempFloatValue = tempFloatValue
    }

    companion object {
        const val KEY_START_TIME = "startTime"
        const val KEY_STEP_VALUE = "stepValue"
        const val KEY_HEART_VALUE = "heartValue"
        const val KEY_DBP_VALUE = "DBPValue"
        const val KEY_SBP_VALUE = "SBPValue"
        const val KEY_OOV_VALUE = "OOValue"
        const val KEY_RESPIRATORY_RATE_VALUE = "respiratoryRateValue"
        const val KEY_HRV_VALUE = "hrvValue"
        const val KEY_CVRR_VALUE = "cvrrValue"
        const val KEY_TEMP_INT_VALUE = "tempIntValue"
        const val KEY_TEMP_FLOAT_VALUE = "tempFloatValue"
    }
}