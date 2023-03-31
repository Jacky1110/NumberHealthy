package com.jotangi.NumberHealthy.utils.smartwatch.model

object YCBTDataHelper {
    fun YCBTDataResponseFromMap(map: HashMap<String?, Any?>?): YCBTDataResponse? {
        if (map == null) return null
        val keys = arrayOf(
            YCBTDataResponse.KEY_CODE,
            YCBTDataResponse.KEY_DATA_TYPE,
            YCBTDataResponse.KEY_DATA
        )
        try {
            if (hasKeys(map, keys)) {
                val obj1 = map[YCBTDataResponse.KEY_CODE]
                val code = obj1?.toString()?.toInt() ?: 0
                val obj2 = map[YCBTDataResponse.KEY_DATA_TYPE]
                val datatype = obj2?.toString()?.toInt() ?: 0
                val obj3 = map[YCBTDataResponse.KEY_DATA]
                if (obj3 is List<*>) return YCBTDataResponse(code, datatype, obj3 as List<Any?>?)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun historyHeartInfosListFromObject(objlist: List<Any?>?): List<HistoryHeartInfo>? {
        if (objlist == null) return null
        try {
            val hislist: MutableList<HistoryHeartInfo> = ArrayList()
            for (i in objlist.indices) {
                val mapobj = objlist[i]
                if (mapobj is Map<*, *>) {
                    val info = historyHeartInfoFromMap(mapobj as Map<String?, Any?>?)
                    if (info != null) hislist.add(info) else return null
                }
            }
            return hislist
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun historyHeartInfoFromMap(map: Map<String?, Any?>?): HistoryHeartInfo? {
        if (map == null) return null
        val keys = arrayOf(
            HistoryHeartInfo.KEY_HEART_START_TIME,
            HistoryHeartInfo.KEY_HEART_VALUE
        )
        try {
            if (hasKeys(map, keys)) {
                val obj1 = map[HistoryHeartInfo.KEY_HEART_START_TIME]
                val heartstarttime = obj1?.toString()?.toLong() ?: 0
                val obj2 = map[HistoryHeartInfo.KEY_HEART_VALUE]
                val heartvalue = obj2?.toString()?.toInt() ?: 0
                return HistoryHeartInfo(heartstarttime, heartvalue)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }


    fun historyBloodInfosListFromObject(objlist: List<Any?>?): List<HistoryBloodBPInfo>? {
        if (objlist == null) return null
        try {
            val hislist: MutableList<HistoryBloodBPInfo> = ArrayList()
            for (i in objlist.indices) {
                val mapobj = objlist[i]
                if (mapobj is Map<*, *>) {
                    val info = historyBloodInfoFromMap(mapobj as Map<String?, Any?>?)
                    if (info != null) hislist.add(info) else return null
                }
            }
            return hislist
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun historyBloodInfoFromMap(map: Map<String?, Any?>?): HistoryBloodBPInfo? {
        if (map == null) return null
        val keys = arrayOf(
            HistoryBloodBPInfo.KEY_BLOOD_START_TIME,
            HistoryBloodBPInfo.KEY_BLOOD_DBP,
            HistoryBloodBPInfo.KEY_BLOOD_SBP
        )
        try {
            if (hasKeys(map, keys)) {
                val obj1 = map[HistoryBloodBPInfo.KEY_BLOOD_START_TIME]
                val bloodstarttime = obj1?.toString()?.toLong() ?: 0
                val obj2 = map[HistoryBloodBPInfo.KEY_BLOOD_DBP]
                val blooddbp = obj2?.toString()?.toInt() ?: 0
                val obj3 = map[HistoryBloodBPInfo.KEY_BLOOD_SBP]
                val bloodsbp = obj3?.toString()?.toInt() ?: 0
                return HistoryBloodBPInfo(bloodstarttime, blooddbp.toInt(), bloodsbp)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun historySleepInfoListFromObject(objlist: List<Any?>?): List<HistorySleepInfo>? {
        if (objlist == null) return null
        try {
            val hislist: MutableList<HistorySleepInfo> = ArrayList()
            for (i in objlist.indices) {
                val mapobj = objlist[i]
                if (mapobj is Map<*, *>) {
                    val info = historySleepInfoFromMap(mapobj as Map<String?, Any?>?)
                    if (info != null) hislist.add(info) else return null
                }
            }
            return hislist
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun historySleepInfoFromMap(map: Map<String?, Any?>?): HistorySleepInfo? {
        if (map == null) return null
        val keys = arrayOf(
            HistorySleepInfo.KEY_START_TIME,
            HistorySleepInfo.KEY_END_TIME,
            HistorySleepInfo.KEY_DEEP_SLEEP_COUNT,
            HistorySleepInfo.KEY_LIGHT_SLEEP_COUNT,
            HistorySleepInfo.KEY_DEEP_SLEEP_TOTAL,
            HistorySleepInfo.KEY_LIGHT_SLEEP_TOTAL,
            HistorySleepInfo.KEY_SLEEP_DATA
        )
        try {
            if (hasKeys(map, keys)) {
                val obj1 = map[HistorySleepInfo.KEY_START_TIME]
                val starttime = obj1?.toString()?.toLong() ?: 0
                val obj2 = map[HistorySleepInfo.KEY_END_TIME]
                val endtime = obj2?.toString()?.toLong() ?: 0
                val obj3 = map[HistorySleepInfo.KEY_DEEP_SLEEP_COUNT]
                val deepsleepcount = obj3?.toString()?.toInt() ?: 0
                val obj4 = map[HistorySleepInfo.KEY_LIGHT_SLEEP_COUNT]
                val lightsleepcount = obj4?.toString()?.toInt() ?: 0
                val obj5 = map[HistorySleepInfo.KEY_DEEP_SLEEP_TOTAL]
                val deepsleeptotal = obj5?.toString()?.toInt() ?: 0
                val obj6 = map[HistorySleepInfo.KEY_LIGHT_SLEEP_TOTAL]
                val lightsleeptotal = obj6?.toString()?.toInt() ?: 0
                val datalist = map[HistorySleepInfo.KEY_SLEEP_DATA] as List<Any?>?
//                val obj7 = map[HistorySleepInfo.KEY_SLEEP_TYPE]
//                val sleepType= obj7?.toString() ?: ""
//                val obj8 = map[HistorySleepInfo.KEY_SLEEP_START_TIME]
//                val sleepStartTime = obj8?.toString()?.toLong() ?: 0
//                val obj9 = map[HistorySleepInfo.KEY_SLEEP_LEN]
//                val sleepLen = obj9?.toString()?.toLong() ?: 0
                if (datalist != null) {
                    val detailInfoList = historySleepDetailInfoListFromObject(datalist)
                    return HistorySleepInfo(
                        starttime,
                        endtime,
                        deepsleepcount,
                        lightsleepcount,
                        deepsleeptotal,
                        lightsleeptotal,
                        detailInfoList
//                        sleepType,
//                        sleepStartTime,
//                        sleepLen
                    )
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun historySleepDetailInfoListFromObject(objlist: List<Any?>?): List<HistorySleepDetailInfo>? {
        if (objlist == null) return null
        try {
            val hislist: MutableList<HistorySleepDetailInfo> = ArrayList()
            for (i in objlist.indices) {
                val mapobj = objlist[i]
                if (mapobj is Map<*, *>) {
                    val info = historySleepDetailInfoFromMap(mapobj as Map<String?, Any?>?)
                    if (info != null) hislist.add(info) else return null
                }
            }
            return hislist
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun historySleepDetailInfoFromMap(map: Map<String?, Any?>?): HistorySleepDetailInfo? {
        if (map == null) return null
        val keys = arrayOf(
            HistorySleepDetailInfo.KEY_SLEEP_TYPE,
            HistorySleepDetailInfo.KEY_SLEEP_START_TIME,
            HistorySleepDetailInfo.KEY_SLEEP_LEN
        )
        try {
            if (hasKeys(map, keys)) {
                val obj1 = map[HistorySleepDetailInfo.KEY_SLEEP_TYPE]
                val sleeptype = obj1?.toString()?.toInt() ?: 0
                val obj2 = map[HistorySleepDetailInfo.KEY_SLEEP_START_TIME]
                val starttime = obj2?.toString()?.toLong() ?: 0
                val obj3 = map[HistorySleepDetailInfo.KEY_SLEEP_LEN]
                val sleeplen = obj3?.toString()?.toInt() ?: 0
                return HistorySleepDetailInfo(sleeptype, starttime, sleeplen)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun historyHealthInfosListFromObject(objlist: List<Any?>?): List<HistoryHealthInfo>? {
        if (objlist == null) return null
        try {
            val hislist: MutableList<HistoryHealthInfo> = ArrayList()
            for (i in objlist.indices) {
                val mapobj = objlist[i]
                if (mapobj is Map<*, *>) {
                    val info = historyHealthInfoFromMap(mapobj as Map<String?, Any?>?)
                    if (info != null) hislist.add(info) else return null
                }
            }
            return hislist
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun historyHealthInfoFromMap(map: Map<String?, Any?>?): HistoryHealthInfo? {
        if (map == null) return null
        val keys = arrayOf(
            HistoryHealthInfo.KEY_START_TIME,
            HistoryHealthInfo.KEY_STEP_VALUE,
            HistoryHealthInfo.KEY_HEART_VALUE,
            HistoryHealthInfo.KEY_DBP_VALUE,
            HistoryHealthInfo.KEY_SBP_VALUE,
            HistoryHealthInfo.KEY_OOV_VALUE,
            HistoryHealthInfo.KEY_RESPIRATORY_RATE_VALUE,
            HistoryHealthInfo.KEY_TEMP_FLOAT_VALUE,
            HistoryHealthInfo.KEY_TEMP_INT_VALUE,
            HistoryHealthInfo.KEY_CVRR_VALUE,
            HistoryHealthInfo.KEY_HRV_VALUE
        )
        try {
            if (hasKeys(map, keys)) {
                var obj = map[HistoryHealthInfo.KEY_START_TIME]
                val starttime = obj?.toString()?.toLong() ?: 0
                obj = map[HistoryHealthInfo.KEY_STEP_VALUE]
                val step = obj?.toString()?.toInt() ?: 0
                obj = map[HistoryHealthInfo.KEY_HEART_VALUE]
                val hr = obj?.toString()?.toInt() ?: 0
                obj = map[HistoryHealthInfo.KEY_DBP_VALUE]
                val dbp = obj?.toString()?.toInt() ?: 0
                obj = map[HistoryHealthInfo.KEY_SBP_VALUE]
                val sbp = obj?.toString()?.toInt() ?: 0
                obj = map[HistoryHealthInfo.KEY_OOV_VALUE]
                val oov = obj?.toString()?.toInt() ?: 0
                obj = map[HistoryHealthInfo.KEY_RESPIRATORY_RATE_VALUE]
                val respirate = obj?.toString()?.toInt() ?: 0
                obj = map[HistoryHealthInfo.KEY_TEMP_FLOAT_VALUE]
                val tempfloat = obj?.toString()?.toInt() ?: 0
                obj = map[HistoryHealthInfo.KEY_TEMP_INT_VALUE]
                val tempint = obj?.toString()?.toInt() ?: 0
                obj = map[HistoryHealthInfo.KEY_CVRR_VALUE]
                val cvrr = obj?.toString()?.toInt() ?: 0
                obj = map[HistoryHealthInfo.KEY_HRV_VALUE]
                val hrv = obj?.toString()?.toInt() ?: 0
                return HistoryHealthInfo(
                    starttime,
                    step,
                    hr,
                    dbp,
                    sbp,
                    oov,
                    respirate,
                    hrv,
                    cvrr,
                    tempint,
                    tempfloat
                )
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun historySportInfoListFromObject(objlist: List<Any?>?): List<HistorySportInfo>? {
        if (objlist == null) return null
        try {
            val hislist: MutableList<HistorySportInfo> = ArrayList()
            for (i in objlist.indices) {
                val mapobj = objlist[i]
                if (mapobj is Map<*, *>) {
                    val info = historySportInfoFromMap(mapobj as Map<String?, Any?>?)
                    if (info != null) hislist.add(info) else return null
                }
            }
            return hislist
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun historySportInfoFromMap(map: Map<String?, Any?>?): HistorySportInfo? {
        if (map == null) return null
        val keys = arrayOf(
            HistorySportInfo.KEY_SPORT_START_TIME,
            HistorySportInfo.KEY_SPORT_END_TIME,
            HistorySportInfo.KEY_SPORT_STEP,
            HistorySportInfo.KEY_SPORT_DISTANCE,
            HistorySportInfo.KEY_SPORT_CALORIE
        )
        try {
            if (hasKeys(map, keys)) {
                val obj1 = map[HistorySportInfo.KEY_SPORT_START_TIME]
                val starttime = obj1?.toString()?.toLong() ?: 0
                val obj2 = map[HistorySportInfo.KEY_SPORT_END_TIME]
                val endtime = obj2?.toString()?.toLong() ?: 0
                val obj3 = map[HistorySportInfo.KEY_SPORT_STEP]
                val step = obj3?.toString()?.toInt() ?: 0
                val obj4 = map[HistorySportInfo.KEY_SPORT_DISTANCE]
                val distance = obj4?.toString()?.toInt() ?: 0
                val obj5 = map[HistorySportInfo.KEY_SPORT_CALORIE]
                val calorie = obj5?.toString()?.toInt() ?: 0
                return HistorySportInfo(starttime, endtime, step, distance, calorie)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun hasKeys(map: Map<String?, Any?>?, keys: Array<String>): Boolean {
        for (k in keys) {
            if (map != null) {
                if (!map.containsKey(k)) return false
            }
        }
        return true
    }
}