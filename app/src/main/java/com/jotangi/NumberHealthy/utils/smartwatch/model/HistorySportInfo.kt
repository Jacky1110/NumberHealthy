package com.jotangi.NumberHealthy.utils.smartwatch.model

import com.google.gson.annotations.SerializedName

class HistorySportInfo {
    @SerializedName(KEY_SPORT_START_TIME)
    var sportStartTime: Long = 0

    @SerializedName(KEY_SPORT_END_TIME)
    var sportEndTime: Long = 0

    @SerializedName(KEY_SPORT_STEP)
    var sportStep = 0

    @SerializedName(KEY_SPORT_DISTANCE)
    var sportDistance = 0

    @SerializedName(KEY_SPORT_CALORIE)
    var sportCalorie = 0

    constructor() {}
    constructor(
        sportStartTime: Long,
        sportEndTime: Long,
        sportStep: Int,
        sportDistance: Int,
        sportCalorie: Int
    ) {
        this.sportStartTime = sportStartTime
        this.sportEndTime = sportEndTime
        this.sportStep = sportStep
        this.sportDistance = sportDistance
        this.sportCalorie = sportCalorie
    }

    companion object {
        const val KEY_SPORT_START_TIME = "sportStartTime"
        const val KEY_SPORT_END_TIME = "sportEndTime"
        const val KEY_SPORT_STEP = "sportStep"
        const val KEY_SPORT_DISTANCE = "sportDistance"
        const val KEY_SPORT_CALORIE = "sportCalorie"
    }
}