package com.jotangi.NumberHealthy.utils.smartwatch.model

import com.google.gson.annotations.SerializedName

class HistoryBloodBPInfo {
    @SerializedName(KEY_BLOOD_START_TIME)
    var bloodStartTime // 血压测试时间
            : Long = 0

    @SerializedName(KEY_BLOOD_DBP)
    var bloodDBP // 收缩压
            = 0

    @SerializedName(KEY_BLOOD_SBP)
    var bloodSBP // 舒张压
            = 0

    constructor() {}
    constructor(bloodStartTime: Long, bloodDBP: Int, bloodSBP: Int) {
        this.bloodStartTime = bloodStartTime
        this.bloodDBP = bloodDBP
        this.bloodSBP = bloodSBP
    }

    companion object {
        const val KEY_BLOOD_START_TIME = "bloodStartTime"
        const val KEY_BLOOD_DBP = "bloodDBP"
        const val KEY_BLOOD_SBP = "bloodSBP"
    }
}