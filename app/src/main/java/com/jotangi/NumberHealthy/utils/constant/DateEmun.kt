package com.jotangi.NumberHealthy.utils.constant


/**
 * Date 種類
 */
enum class DateType(val value: String) {
    YMD_HMS("yyyy-MM-dd HH:mm:ss"),
    YMD_HM("yyyy-MM-dd HH:mm"),
    YMD("yyyy-MM-dd"),
    MD("MM/dd"),
    HM("HH:mm")
}


/**
 * sleep 邏輯
 */
enum class SleepElement(val value: String) {
    BaseTime("17:00:00")
}