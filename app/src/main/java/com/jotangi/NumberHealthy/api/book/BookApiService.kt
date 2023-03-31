package com.jotangi.NumberHealthy.api.book

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.jotangi.NumberHealthy.StoreMangerUi.googlevision.StationListResponse
import okhttp3.RequestBody
import retrofit2.http.*


interface BookApiService {

    /**
     * (1)	Tours app會員註冊
     */
    @FormUrlEncoded
    @POST("user_register.php")
    suspend fun userRegister(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("member_name") name: String?,
        @Field("member_type") type: String?,
    ): BookDataResponse

    /**
     * (2)	Tours app會員登入
     */
    @FormUrlEncoded
    @POST("user_login.php")
    suspend fun userLogin(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("FCM_Token") token: String?,
        @Field("unique_id") uniqueId: String?,
    ): BookDataResponse

    /**
     * Unique_id 查詢會員帳號密碼
     */
    @FormUrlEncoded
    @POST("get_uidpwd.php")
    suspend fun getUidpwd(
        @Field("unique_id") id: String
    ): GetUidpwdResponse

    /**
     * (3)	Tours app會員登出
     */
    @FormUrlEncoded
    @POST("user_logout.php")
    suspend fun userLogout(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String
    ): BookDataResponse

    /**
     * (4)	Tours app會員變更密碼
     */
    @FormUrlEncoded
    @POST("user_changepwd.php")
    suspend fun userChangePwd(
        @Field("member_id") id: String,
        @Field("old_password") pwd: String,
        @Field("new_password") token: String
    ): BookDataResponse

    /**
     * (5)	Tours app會員忘記密碼驗證
     */
    @FormUrlEncoded
    @POST("user_code.php")
    suspend fun userCode(
        @Field("member_id") id: String,
    ): BookDataResponse

    /**
     * (6)	Tours app會員忘記密碼
     */
    @FormUrlEncoded
    @POST("user_resetpwd.php")
    suspend fun userResetPwd(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("code") code: String
    ): BookDataResponse


    /**
     * (8)	Tours app會員資料異動
     */
    @FormUrlEncoded
    @POST("user_edit.php")
    suspend fun userEdit(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("member_name") name: String,
        @Field("member_gender") gender: String,
        @Field("member_email") email: String,
        @Field("member_birthday") birthday: String,
        @Field("member_address") address: String?,
        @Field("member_phone") phone: String?,
    ): BookDataResponse

    @FormUrlEncoded
    @POST("member_info3.php")
    suspend fun memberInfo(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String
    ): MemberInfoData

    @FormUrlEncoded
    @POST("coupon_list2.php")
    suspend fun getCouponList(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String
    ): List<GetCouponDataBeen>

    @FormUrlEncoded
    @POST("get_member_point.php")
    suspend fun getCouponPoint(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String
    ): GetCouponPointBeen

    @FormUrlEncoded
    @POST("apply_coupon3.php")
    suspend fun getScanCoupon(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("customer_id") customerId: String,
        @Field("coupon_id") couponId: String,
        @Field("coupon_count") couponCount: String,
    ): BookDataResponse

    @FormUrlEncoded
    @POST("storeapply_list.php")
    suspend fun getHistoryList(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("apply_startdate") startdate: String,
        @Field("apply_enddate") enddate: String,
    ): List<GetStoreApplyListBeen>


    @FormUrlEncoded
    @POST("get_evaluationlog.php")
    suspend fun getEvaluationLogList(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("log_startdate") startdate: String,
        @Field("log_enddate") enddate: String,
    ): List<GetEvaluationLogListBeen>

    @FormUrlEncoded
    @POST("get_activitylog.php")
    suspend fun getActivityLogList(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("log_startdate") startdate: String,
        @Field("log_enddate") enddate: String,
    ): List<GetActivityLogListBeen>


    @POST("user_uploadpic.php")
    suspend fun userUploadpic(
        @Body body: RequestBody
    ): BookDataResponse

//    @Multipart
//    @POST("user_uploadpic.php")
//    suspend fun userUploadpic(
//        @PartMap params: MutableMap<String, RequestBody?>
//    ): BookDataResponse

    @FormUrlEncoded
    @POST("station_list.php")
    suspend fun getStationList(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String
    ): List<StationListData>

    @FormUrlEncoded
    @POST("station_info.php")
    suspend fun getStationInfo(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("sid") sid: String
    ): List<StationInfoData>

    @FormUrlEncoded
    @POST("get_stationservice.php")
    suspend fun getStationService(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("sid") sid: String
    ): List<StationServiceData>

    @FormUrlEncoded
    @POST("is_bookingday2.php")
    suspend fun getBookingDay(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("sid") sid: String,
        @Field("reserve_date") reserve_date: String,
        @Field("service_code") service_code: String
    ): BookingDayListData

    @FormUrlEncoded
    @POST("add_booking.php")
    suspend fun addBooking(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("sid") sid: String,
        @Field("reserve_date") reserve_date: String,
        @Field("reserve_time") reserve_time: String,
        @Field("service_item") service_item: String
    ): AddBookingData

    @FormUrlEncoded
    @POST("booking_list.php")
    suspend fun getBookingList(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String
    ): List<BookingRecordData>

    @FormUrlEncoded
    @POST("booking_info.php")
    suspend fun getBookingInfo(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("booking_no") booking_no: String
    ): List<BookingInfoData>

    @FormUrlEncoded
    @POST("booking_cancel.php")
    suspend fun cancelBooking(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("booking_no") booking_no: String
    ): CancelBookingData

    @FormUrlEncoded
    @POST("booking_cancel2.php")
    suspend fun cancelBooking2(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("booking_no") booking_no: String
    ): CancelBookingData

    @FormUrlEncoded
    @POST("get_workingday2.php")
    suspend fun get_workingday2(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("sid") sid: String,
        @Field("service_code") service_code: String,
        @Field("start_date") start_date: String
    ): WorkingDay2Data


    @FormUrlEncoded
    @POST("activity_info.php")
    suspend fun activity_info(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("aid") aid: String,
    ): List<AcInResponse>

    @FormUrlEncoded
    @POST("store_bookinglist.php")
    suspend fun getSMlist(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String
    ): List<StationListResponse>

    //2022/4/29 add
    @FormUrlEncoded
    @POST("hospital_list.php")
    suspend fun getHospitalList(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String
    ): List<StationListData>

    @FormUrlEncoded
    @POST("hospital_info.php")
    suspend fun getHospitalInfo(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("sid") sid: String
    ): List<StationInfoData>

    @FormUrlEncoded
    @POST("division_list.php")
    suspend fun getDivisionList(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("sid") sid: String
    ): List<DivisionListData>

    @FormUrlEncoded
    @POST("physician_list.php")
    suspend fun getPhysicianList(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("sid") sid: String,
        @Field("did") did: String
    ): List<PhysicianListData>

    @FormUrlEncoded
    @POST("get_workingday4.php")
    suspend fun getPhysicianWorkingDay(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("sid") sid: String,
        @Field("pid") did: String,
        @Field("start_date") start_date: String
    ): PhysicianWorkingDayListData

    @FormUrlEncoded
    @POST("add_booking4.php")
    suspend fun addVideoReserveOrder(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("sid") sid: String,
        @Field("did") did: String,
        @Field("pid") pid: String,
        @Field("reserve_date") reserve_date: String,
        @Field("reserve_time") reserve_time: String,
        @Field("reserve_endtime") reserve_endtime: String,
        @Field("price") price: String,
        @Field("member_phone") member_phone: String,
        @Field("member_name") member_name: String,
        @Field("member_email") member_email: String,
        @Field("question") question: String
    ): CommonData

    @FormUrlEncoded
    @POST("booking_list4.php")
    suspend fun videoRecordList(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String
    ): List<VideoRecordListData>

    @FormUrlEncoded
    @POST("booking_cancel4.php")
    suspend fun videoRecordCancel(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("booking_no") booking_no: String
    ): CommonData

    @FormUrlEncoded
    @POST("outpatient_info2.php")
    suspend fun videoRecordOutpatient(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("booking_no") booking_no: String
    ): List<VideoRecordOutpatientData>

    @POST("add_booking2.php")
    suspend fun addBooking2(
        @Body Request: RequestBody
    ): AddBookingData

    @FormUrlEncoded
    @POST("booking_list2.php")
    suspend fun getBookingList2(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String
    ): List<BookingRecordData>

    @FormUrlEncoded
    @POST("booking_info2.php")
    suspend fun getBookingInfo2(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("booking_no") booking_no: String
    ): List<BookingInfoData>

    @FormUrlEncoded
    @POST("upload_messagelog.php")
    suspend fun upload_messagelog(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("booking_no") booking_no: String,
        @Field("message_text") text: String,
    ): messagelogData

    @POST("upload_messagepic.php")
    suspend fun upload_messagepic(
        @Body Request: RequestBody
    ): messagelogData

    @FormUrlEncoded
    @POST("get_workingday3.php")
    suspend fun get_workingday3(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("sid") sid: String,
    ): WorkingDay2Data

    @FormUrlEncoded
    @POST("outpatient_info.php")
    suspend fun outpatient_info(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("booking_no") booking_no: String
    ): List<BookingInfoData>

    @FormUrlEncoded
    @POST("reschedule.php")
    suspend fun reschedule(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("booking_no") booking_no: String
    ): CancelBookingData

    @FormUrlEncoded
    @POST("updatebooking_status.php")
    suspend fun updatebooking_status(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("booking_no") booking_no: String,
        @Field("reserve_status") reserve_status: String
    ): BaseBookResponse


    // 關懷
    @FormUrlEncoded
    @POST("add_carelist.php")
    suspend fun addCarelist(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("cmember_id") cId: String
    ): AddCarelistData

    @FormUrlEncoded
    @POST("carelist_info.php")
    suspend fun getCareList(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
    ): List<CareListVO>

    @FormUrlEncoded
    @POST("becared_list.php")
    suspend fun getCaredList(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
    ): List<CareListVO>

    @FormUrlEncoded
    @POST("upd_nickname.php")
    suspend fun updateCareNickName(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("nick_name") nickName: String,
        @Field("cid") cId: String
    ): UpdateCareNickName

    @FormUrlEncoded
    @POST("del_carelist.php")
    suspend fun deleteCareMember(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("cid") cId: String
    ): DeleteCareMember

    @FormUrlEncoded
    @POST("del_becaredlist.php")
    suspend fun cancelCaredMember(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("cid") cId: String,
        @Field("sms") sms: String
    ): CancelCaredMember

    @FormUrlEncoded
    @POST("msgbox_list.php")
    suspend fun getMsgBoxList(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String
    ): List<NotifyHistoryListVO>

    @FormUrlEncoded
    @POST("msgbox_count.php")
    suspend fun getMessageBoxCount(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String
    ): MessageBoxCountVO

    @FormUrlEncoded
    @POST("upd_msgbox.php")
    suspend fun updateMessageReadStatus(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("rid") rId: String
    ): MessageBoxStatus

    @FormUrlEncoded
    @POST("upd_carelist.php")
    suspend fun authCareStatus(
        @Field("member_id") id: String,
        @Field("member_pwd") pwd: String,
        @Field("status") status: String,
        @Field("message_from") messageFrom: String
    ): AuthCareStatus
}