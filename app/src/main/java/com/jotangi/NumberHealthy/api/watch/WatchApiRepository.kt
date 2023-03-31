package com.jotangi.NumberHealthy.api.watch

import com.jotangi.NumberHealthy.api.AppClientManager
import com.jotangi.NumberHealthy.ui.mylittlemin.BaseBookRequest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody


class WatchApiRepository {

    /**
     * 01. App 上傳會員手環的步數資料
     */
    suspend fun uploadSteps(request: UploadStepRequest): WatchDataResponse {
        val map: MutableMap<String, RequestBody?> = HashMap()
        map["memberId"] = toRequestBody(request.memberId)
        map["sportStartTime"] = toRequestBody(request.sportStartTime)
        map["sportEndTime"] = toRequestBody(request.sportEndTime)
        map["sportStep"] = toRequestBody(request.sportStep.toString())
        map["sportCalorie"] = toRequestBody(request.sportCalorie.toString())
        map["sportDistance"] = toRequestBody(request.sportDistance.toString())

        return AppClientManager.instance.watchService.uploadSteps(map)
    }

    /**
     * 02. App 上傳會員手環的睡眠資料
     */
    suspend fun uploadSleep(request: UploadSleepRequest): WatchDataResponse {
        val map: MutableMap<String, RequestBody?> = HashMap()
        map["memberId"] = toRequestBody(request.memberId)
        map["startTime"] = toRequestBody(request.startTime)
        map["endTime"] = toRequestBody(request.endTime)
        map["deepSleepCount"] = toRequestBody(request.deepSleepCount.toString())
        map["lightSleepCount"] = toRequestBody(request.lightSleepCount.toString())
        map["deepSleepTotal"] = toRequestBody(request.deepSleepTotal.toString())
        map["lightSleepTotal"] = toRequestBody(request.lightSleepTotal.toString())
        map["sleepData"] = toRequestBody(request.sleepData)

        return AppClientManager.instance.watchService.uploadSleep(map)
    }

    /**
     * 03. App 上傳會員手環的心率資料
     */
    suspend fun uploadHr(request: UploadHrRequest): WatchDataResponse {
        val map: MutableMap<String, RequestBody?> = HashMap()
        map["memberId"] = toRequestBody(request.memberId)
        map["heartStartTime"] = toRequestBody(request.heartStartTime)
        map["heartValue"] = toRequestBody(request.heartValue.toString())
        map["dataType"] = toRequestBody(request.dataType.toString())

        return AppClientManager.instance.watchService.uploadHr(map)
    }

    /**
     * 04. App 上傳會員手環的血壓資料
     */
    suspend fun uploadBp(request: UploadBPRequest): WatchDataResponse {
        val map: MutableMap<String, RequestBody?> = HashMap()
        map["memberId"] = toRequestBody(request.memberId)
        map["bloodStartTime"] = toRequestBody(request.bloodStartTime)
        map["bloodDBP"] = toRequestBody(request.bloodDBP.toString())
        map["bloodSBP"] = toRequestBody(request.bloodSBP.toString())
        map["dataType"] = toRequestBody(request.dataType.toString())

        return AppClientManager.instance.watchService.uploadBp(map)
    }

    /**
     * 05. App 上傳會員手環的血氧數值
     */
    suspend fun uploadOxygen(request: UploadOxygenRequest): WatchDataResponse {
        val map: MutableMap<String, RequestBody?> = HashMap()
        map["memberId"] = toRequestBody(request.memberId)
        map["startTime"] = toRequestBody(request.startTime)
        map["OOValue"] = toRequestBody(request.OOValue.toString())
        map["dataType"] = toRequestBody(request.dataType.toString())

        return AppClientManager.instance.watchService.uploadOxygen(map)
    }

    /**
     * 07. App 取得步數資料
     */
    suspend fun getSteps(request: ElementRequest): GetStepsData {
        return try {
            AppClientManager.instance.watchService.getSteps(
                request.memberId,
                request.startTime,
                request.endTime
            )
        } catch (e: Exception) {
            GetStepsData()
        }
    }

    /**
     * 08. App 取得睡眠數據
     */
    suspend fun getSleep(request: ElementRequest): GetSleepResponse {
        return try {
            AppClientManager.instance.watchService.getSleep(
                request.memberId,
                request.startTime,
                request.endTime
            )
        } catch (e: Exception) {
            GetSleepResponse()
        }
    }

    /**
     * 09. App 取得睡眠細節
     */
    suspend fun getSleepData(request: ElementRequest): SleepDataResponse {
        return try {
            AppClientManager.instance.watchService.getSleepData(
                request.memberId,
                request.startTime,
                request.endTime
            )
        } catch (e: Exception) {
            SleepDataResponse()
        }
    }

    /**
     * 10. App 取得心率數值
     */
    suspend fun getHeartRate(request: ElementRequest): HeartRateResponse {
        return try {
            AppClientManager.instance.watchService.getHeartRate(
                request.memberId,
                request.startTime,
                request.endTime
            )
        } catch (e: Exception) {
            HeartRateResponse()
        }
    }

    /**
     * 11. App 取得血壓數值
     */
    suspend fun getBP(request: ElementRequest): BPResponse {
        return try {
            AppClientManager.instance.watchService.getBP(
                request.memberId,
                request.startTime,
                request.endTime
            )
        } catch (e: Exception) {
            BPResponse()
        }
    }

    /**
     * 13. App 取得血氧數值
     */
    suspend fun getOxygen(request: ElementRequest): OxygenResponse {
        return try {
            AppClientManager.instance.watchService.getOxygen(
                request.memberId,
                request.startTime,
                request.endTime
            )
        } catch (e: Exception) {
            OxygenResponse()
        }
    }

    /**
     * 14. App 上傳會員手環的ECG資料
     */
    suspend fun uploadEcg(request: UploadEcgRequest): WatchDataResponse {
        val map: MutableMap<String, RequestBody?> = HashMap()
        map["memberId"] = toRequestBody(request.memberId)
        map["ecgStartTime"] = toRequestBody(request.ecgStartTime)
        map["ecgValue"] = toRequestBody(request.ecgValue)
        map["hr"] = toRequestBody(request.hr.toString())
        map["dbp"] = toRequestBody(request.dbp.toString())
        map["sbp"] = toRequestBody(request.sbp.toString())
        map["hrv"] = toRequestBody(request.hrv.toString())

        return AppClientManager.instance.watchService.uploadEcg(map)
    }

    /**
     * 15. App 取得ECG數值
     */
    suspend fun getECG(request: ElementRequest): EcgResponse {
        return try {
            AppClientManager.instance.watchService.getECG(
                request.memberId,
                request.startTime,
                request.endTime
            )
        } catch (e: Exception) {
            EcgResponse()
        }
    }

    /**
     * 16. App 上傳會員手環的呼吸率資料
     */
    suspend fun uploadRespiratoryrate(request: UploadRespiratoryRateRequest): WatchDataResponse {
        val map: MutableMap<String, RequestBody?> = HashMap()
        map["memberId"] = toRequestBody(request.memberId)
        map["startTime"] = toRequestBody(request.startTime)
        map["respiratoryrate"] = toRequestBody(request.respiratoryrate.toString())
        map["dataType"] = toRequestBody(request.dataType.toString())

        return AppClientManager.instance.watchService.uploadRespiratoryrate(map)
    }

    /**
     * 17. App 取得呼吸率數值
     */
    suspend fun getBreathRate(request: ElementRequest): BreathRateResponse {
        return try {
            AppClientManager.instance.watchService.getBreathRate(
                request.memberId,
                request.startTime,
                request.endTime
            )
        } catch (e: Exception) {
            BreathRateResponse()
        }
    }

    /**
     * 18. App 上傳會員手環的體溫資料
     */
    suspend fun uploadTemperature(request: UploadTemperatureRequest): WatchDataResponse {
        val map: MutableMap<String, RequestBody?> = HashMap()
        map["memberId"] = toRequestBody(request.memberId)
        map["startTime"] = toRequestBody(request.startTime)
        map["temperature"] = toRequestBody(request.temperature.toString())
        map["dataType"] = toRequestBody(request.dataType.toString())

        return AppClientManager.instance.watchService.uploadTemperature(map)
    }

    /**
     * 19. App 取得體溫數值
     */
    suspend fun getTemperature(request: ElementRequest): TemperatureResponse {
        return try {
            AppClientManager.instance.watchService.getTemperature(
                request.memberId,
                request.startTime,
                request.endTime
            )
        } catch (e: Exception) {
            TemperatureResponse()
        }
    }

    /**
     * 20. App 取得會員手環的保固資料
     */
    suspend fun getWarrantyInfo(deviceNo: String): GetWarrantyInfoResponse {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.watchService.getWarrantyInfo(
                baseRequest.member_id,
                baseRequest.member_pwd,
                deviceNo
            )
        } catch (e: Exception) {
            GetWarrantyInfoResponse()
        }
    }

    /**
     * 21. App 上傳會員骨密的檢查數值
     */
    suspend fun uploadBmd(request: UploadBmdRequest): WatchDataResponse {
        val map: MutableMap<String, RequestBody?> = HashMap()
        map["memberId"] = toRequestBody(request.memberId)
        map["startTime"] = toRequestBody(request.startTime)
        map["TScore"] = toRequestBody(request.TScore)
        map["dataType"] = toRequestBody(request.dataType)

        return AppClientManager.instance.watchService.uploadBmd(map)
    }

    /**
     * 22.App 取得骨密的檢查數值
     */
    suspend fun getBMD(request: ElementRequest): GetBmdData {
        return try {
            AppClientManager.instance.watchService.getBMD(
                request.memberId,
                request.startTime,
                request.endTime
            )
        } catch (e: Exception) {
            GetBmdData()
        }
    }

    /**
     * 23. App 上傳會員黃斑部色素檢查數值
     */
    suspend fun uploadMpod(request: UploadMpodRequest): WatchDataResponse {
        val map: MutableMap<String, RequestBody?> = HashMap()
        map["memberId"] = toRequestBody(request.memberId)
        map["mpodStartTime"] = toRequestBody(request.mpodStartTime)
        map["lefteye"] = toRequestBody(request.lefteye)
        map["righteye"] = toRequestBody(request.righteye)
        map["dataType"] = toRequestBody(request.dataType)

        return AppClientManager.instance.watchService.uploadMpod(map)
    }

    /**
     * 24.App 取得黃斑部色素檢查數值
     */
    suspend fun getMPOD(request: ElementRequest): GetMpodData {
        return try {
            AppClientManager.instance.watchService.getMpod(
                request.memberId,
                request.startTime,
                request.endTime
            )
        } catch (e: Exception) {
            GetMpodData()
        }
    }

    /**
     * 25. App 上傳會員雙臂的血壓資料
     */
    suspend fun uploadBp2(request: UploadArmRequest): WatchDataResponse {
        return try {
            AppClientManager.instance.watchService.uploadBp2(
                request.memberId,
                request.bloodStartTime,
                request.LbloodDBP,
                request.LbloodSBP,
                request.LbloodPP,
                request.LbloodMAP,
                request.RbloodDBP,
                request.RbloodSBP,
                request.RbloodPP,
                request.RbloodMAP,
                request.heartValue,
                request.dataType
            )
        } catch (e: Exception) {
            WatchDataResponse()
        }
    }

    /**
     * 26.App 取得雙臂血壓數值
     */
    suspend fun getBp2(request: ElementRequest): GetBp2Data {
        return try {
            AppClientManager.instance.watchService.getBp2(
                request.memberId,
                request.startTime,
                request.endTime
            )
        } catch (e: Exception) {
            GetBp2Data()
        }
    }

    /**
     * 27. App 上傳會員卡路里的數值
     */
    suspend fun uploadKcal(request: UploadKcalRequest): WatchDataResponse {
        return try {
            AppClientManager.instance.watchService.uploadKcal(
                request.memberId,
                request.startTime,
                request.KCAL,
                request.dataType
            )
        } catch (e: Exception) {
            WatchDataResponse()
        }
    }

    /**
     * 28. App 取得卡路里的數值
     */
    suspend fun getKcal(request: ElementRequest): GetKcalData {
        return try {
            AppClientManager.instance.watchService.getKcal(
                request.memberId,
                request.startTime,
                request.endTime
            )
        } catch (e: Exception) {
            GetKcalData()
        }
    }


    /**
     * 工具
     */
    fun toRequestBody(value: String?): RequestBody? {
        return value?.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it) }
    }
}