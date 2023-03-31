package com.jotangi.NumberHealthy.api.watch

import com.jotangi.NumberHealthy.api.watch.*
import com.jotangi.NumberHealthy.utils.smartwatch.model.*
import okhttp3.RequestBody
import retrofit2.http.*

interface WatchApiService {

    /**
     * 01. App 上傳會員手環的步數資料
     */
    @Multipart
    @POST("JTG_Upload_Steps.php")
    suspend fun uploadSteps(
        @PartMap params: MutableMap<String, RequestBody?>
    ): WatchDataResponse

    /**
     * 02. App 上傳會員手環的睡眠資料
     */
    @Multipart
    @POST("JTG_Upload_Sleep.php")
    suspend fun uploadSleep(
        @PartMap params: MutableMap<String, RequestBody?>
    ): WatchDataResponse

    /**
     * 03. App 上傳會員手環的心率資料
     */
    @Multipart
    @POST("JTG_Upload_HR.php")
    suspend fun uploadHr(
        @PartMap params: MutableMap<String, RequestBody?>
    ): WatchDataResponse

    /**
     * 04. App 上傳會員手環的血壓資料
     */
    @Multipart
    @POST("JTG_Upload_BP.php")
    suspend fun uploadBp(
        @PartMap params: MutableMap<String, RequestBody?>
    ): WatchDataResponse

    /**
     * 05. App 上傳會員手環的血氧數值
     */
    @Multipart
    @POST("JTG_Upload_Oxygen.php")
    suspend fun uploadOxygen(
        @PartMap params: MutableMap<String, RequestBody?>
    ): WatchDataResponse

    /**
     * 07. App 取得步數資料
     */
    @FormUrlEncoded
    @POST("JTG_Get_Steps.php")
    suspend fun getSteps(
        @Field("memberId") memberId: String,
        @Field("startTime") startTime: String,
        @Field("endTime") endTime: String,
    ): GetStepsData

    /**
     * 08. App 取得睡眠數據
     */
    @FormUrlEncoded
    @POST("JTG_Get_Sleep.php")
    suspend fun getSleep(
        @Field("memberId") memberId: String,
        @Field("startTime") startTime: String,
        @Field("endTime") endTime: String
    ): GetSleepResponse

    /**
     * 09. App 取得睡眠細節
     */
    @FormUrlEncoded
    @POST("JTG_Get_SleepData.php")
    suspend fun getSleepData(
        @Field("memberId") memberId: String,
        @Field("startTime") startTime: String,
        @Field("endTime") endTime: String
    ): SleepDataResponse

    /**
     * 10. App 取得心率數值
     */
    @FormUrlEncoded
    @POST("JTG_Get_HR.php")
    suspend fun getHeartRate(
        @Field("memberId") memberId: String,
        @Field("startTime") startTime: String,
        @Field("endTime") endTime: String
    ): HeartRateResponse

    /**
     * 11. App 取得血壓數值
     */
    @FormUrlEncoded
    @POST("JTG_Get_BP.php")
    suspend fun getBP(
        @Field("memberId") memberId: String,
        @Field("startTime") startTime: String,
        @Field("endTime") endTime: String
    ): BPResponse

    /**
     * 13. App 取得血氧數值
     */
    @FormUrlEncoded
    @POST("JTG_Get_Oxygen.php")
    suspend fun getOxygen(
        @Field("memberId") memberId: String,
        @Field("startTime") startTime: String,
        @Field("endTime") endTime: String
    ): OxygenResponse

    /**
     * 14. App 上傳會員手環的ECG資料
     */
    @Multipart
    @POST("JTG_Upload_ECG.php")
    suspend fun uploadEcg(
        @PartMap params: MutableMap<String, RequestBody?>
    ): WatchDataResponse

    /**
     * 15. App 取得ECG數值
     */
    @FormUrlEncoded
    @POST("JTG_Get_ECG.php")
    suspend fun getECG(
        @Field("memberId") memberId: String,
        @Field("startTime") startTime: String,
        @Field("endTime") endTime: String
    ): EcgResponse

    /**
     * 16. App 上傳會員手環的呼吸率資料
     */
    @Multipart
    @POST("JTG_Upload_Respiratoryrate.php")
    suspend fun uploadRespiratoryrate(
        @PartMap params: MutableMap<String, RequestBody?>
    ): WatchDataResponse

    /**
     * 17. App 取得呼吸率數值
     */
    @FormUrlEncoded
    @POST("JTG_Get_Respiratoryrate.php")
    suspend fun getBreathRate(
        @Field("memberId") memberId: String,
        @Field("startTime") startTime: String,
        @Field("endTime") endTime: String
    ): BreathRateResponse

    /**
     * 18. App 上傳會員手環的體溫資料
     */
    @Multipart
    @POST("JTG_Upload_Temperature.php")
    suspend fun uploadTemperature(
        @PartMap params: MutableMap<String, RequestBody?>
    ): WatchDataResponse

    /**
     * 19. App 取得體溫數值
     */
    @FormUrlEncoded
    @POST("JTG_Get_Temperature.php")
    suspend fun getTemperature(
        @Field("memberId") memberId: String,
        @Field("startTime") startTime: String,
        @Field("endTime") endTime: String
    ): TemperatureResponse

    /**
     * 20. App 取得會員手環的保固資料
     */
    @FormUrlEncoded
    @POST("JTG_Get_Warrantyinfo.php")
    suspend fun getWarrantyInfo(
        @Field("memberId") memberId: String,
        @Field("memberPwd") memberPwd: String,
        @Field("deviceNo") deviceNo: String,
    ): GetWarrantyInfoResponse

    /**
     * 21. App 上傳會員骨密的檢查數值
     */
    @Multipart
    @POST("JTG_Upload_BMD.php")
    suspend fun uploadBmd(
        @PartMap params: MutableMap<String, RequestBody?>
    ): WatchDataResponse

    /**
     * 22.App 取得骨密的檢查數值
     */
    @FormUrlEncoded
    @POST("JTG_Get_BMD.php")
    suspend fun getBMD(
        @Field("memberId") memberId: String,
        @Field("startTime") startTime: String,
        @Field("endTime") endTime: String
    ): GetBmdData

    /**
     * 23. App 上傳會員黃斑部色素檢查數值
     */
    @Multipart
    @POST("JTG_Upload_MPOD.php")
    suspend fun uploadMpod(
        @PartMap params: MutableMap<String, RequestBody?>
    ): WatchDataResponse

    /**
     * 24.App 取得黃斑部色素檢查數值
     */
    @FormUrlEncoded
    @POST("JTG_Get_MPOD.php")
    suspend fun getMpod(
        @Field("memberId") memberId: String,
        @Field("startTime") startTime: String,
        @Field("endTime") endTime: String
    ): GetMpodData

    /**
     * 25. App 上傳會員雙臂的血壓資料
     */
    @FormUrlEncoded
    @POST("JTG_Upload_BP2.php")
    suspend fun uploadBp2(
        @Field("memberId") memberId: String,
        @Field("bloodStartTime") bloodStartTime: String,
        @Field("LbloodDBP") LbloodDBP: String,
        @Field("LbloodSBP") LbloodSBP: String,
        @Field("LbloodPP") LbloodPP: String,
        @Field("LbloodMAP") LbloodMAP: String,
        @Field("RbloodDBP") RbloodDBP: String,
        @Field("RbloodSBP") RbloodSBP: String,
        @Field("RbloodPP") RbloodPP: String,
        @Field("RbloodMAP") RbloodMAP: String,
        @Field("heartValue") heartValue: String,
        @Field("dataType") dataType: String,
    ): WatchDataResponse

    /**
     * 26.App 取得雙臂血壓數值
     */
    @FormUrlEncoded
    @POST("JTG_Get_BP2.php")
    suspend fun getBp2(
        @Field("memberId") memberId: String,
        @Field("startTime") startTime: String,
        @Field("endTime") endTime: String
    ): GetBp2Data

    /**
     * 27. App 上傳會員卡路里的數值
     */
    @FormUrlEncoded
    @POST("JTG_Upload_KCAL.php")
    suspend fun uploadKcal(
        @Field("memberId") memberId: String,
        @Field("startTime") startTime: String,
        @Field("KCAL") KCAL: String,
        @Field("dataType") dataType: String,
    ): WatchDataResponse

    /**
     * 28. App 取得卡路里的數值
     */
    @FormUrlEncoded
    @POST("JTG_Get_KCAL.php")
    suspend fun getKcal(
        @Field("memberId") memberId: String,
        @Field("startTime") startTime: String,
        @Field("endTime") endTime: String
    ): GetKcalData
}