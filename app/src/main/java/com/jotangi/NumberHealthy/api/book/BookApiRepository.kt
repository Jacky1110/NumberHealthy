package com.jotangi.NumberHealthy.api.book

import android.util.Log
import com.jotangi.NumberHealthy.StoreMangerUi.googlevision.StationListResponse
import com.jotangi.NumberHealthy.api.AppClientManager
import com.jotangi.NumberHealthy.ui.mylittlemin.*
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class BookApiRepository {

    fun toRequestBody(value: String?): RequestBody? {
        return value?.let { RequestBody.create("text/plain".toMediaTypeOrNull(), it) }
    }

    fun toFileRequestBody(file: File?): RequestBody? {
        return file?.let { RequestBody.create("image/jpg".toMediaTypeOrNull(), it) }
//        return file?.let {
//            val uris: Uri = Uri.fromFile(it)
//            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uris.toString())
//            val mime = MimeTypeMap.getSingleton()
//                .getMimeTypeFromExtension(fileExtension.toLowerCase())
//            RequestBody.create(mime?.toMediaTypeOrNull(), it)
//        }
    }

    /**
     * (1)	Tours app會員註冊
     */
    suspend fun userRegister(request: UserRegister): BookDataResponse {
        return try {
            AppClientManager.instance.bookService.userRegister(
                request.memberId,
                request.memberPwd,
                request.memberName,
                request.memberType
            )
        } catch (e: Exception) {
            BookDataResponse()
        }
    }

    /**
     * (2)	Tours app會員登入
     */
    suspend fun userLogin(userLoginRequest: UserLoginRequest): BookDataResponse {
        return try {
            AppClientManager.instance.bookService.userLogin(
                userLoginRequest.memberId,
                userLoginRequest.memberPwd,
                userLoginRequest.fcmToken,
                userLoginRequest.unique_id
            )
        } catch (e: Exception) {
            BookDataResponse()
        }
    }

    /**
     * Unique_id 查詢會員帳號密碼
     */
    suspend fun getUidpwd(uniqueId: String): GetUidpwdResponse {
        return try {
            AppClientManager.instance.bookService.getUidpwd(uniqueId)
        } catch (e: Exception) {
            GetUidpwdResponse()
        }
    }

    /**
     * (3)	Tours app會員登出
     */
    suspend fun userLogout(): BookDataResponse {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.userLogout(
                baseRequest.member_id,
                baseRequest.member_pwd
            )
        } catch (e: Exception) {
            BookDataResponse()
        }
    }

    /**
     * (4)	Tours app會員變更密碼
     */
    suspend fun userChangePwd(newPassword: String): BookDataResponse {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.userChangePwd(
                baseRequest.member_id,
                baseRequest.member_pwd,
                newPassword
            )
        } catch (e: Exception) {
            BookDataResponse()
        }
    }

    /**
     * (5)	Tours app會員忘記密碼驗證
     */
    suspend fun userCode(memberId: String): BookDataResponse {
        return try {
            AppClientManager.instance.bookService.userCode(
                memberId,
            )
        } catch (e: Exception) {
            BookDataResponse()
        }
    }


    /**
     * (6)	Tours app會員忘記密碼
     */
    suspend fun userResetPwd(request: UserResetPwd): BookDataResponse {
        return try {
            AppClientManager.instance.bookService.userResetPwd(
                request.memberId,
                request.memberPwd,
                request.code
            )
        } catch (e: Exception) {
            BookDataResponse()
        }
    }

    /**
     * (8)	Tours app會員資料異動
     */
    suspend fun userEdit(request: UserEditRequest): BookDataResponse {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.userEdit(
                baseRequest.member_id,
                baseRequest.member_pwd,
                request.name,
                request.gender,
                request.email,
                request.birthday,
                request.address,
                request.phone
            )
        } catch (e: Exception) {
            BookDataResponse()
        }
    }

    suspend fun memberInfo(): MemberInfoData {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.memberInfo(
                baseRequest.member_id,
                baseRequest.member_pwd
            )
        } catch (e: Exception) {
            MemberInfoData()
        }
    }

    suspend fun getCouponList(): List<GetCouponDataBeen> {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getCouponList(
                baseRequest.member_id,
                baseRequest.member_pwd
            )
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    suspend fun getCouponPoint(): GetCouponPointBeen {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getCouponPoint(
                baseRequest.member_id,
                baseRequest.member_pwd
            )
        } catch (e: Exception) {
            GetCouponPointBeen()
        }
    }

    suspend fun getScanCoupon(
        customerId: String,
        couponId: String,
        couponCount: String
    ): BookDataResponse {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getScanCoupon(
                baseRequest.member_id,
                baseRequest.member_pwd,
                customerId,
                couponId,
                couponCount
            )
        } catch (e: Exception) {
            BookDataResponse()
        }
    }

    suspend fun getHistoryList(startdate: String, enddate: String): List<GetStoreApplyListBeen> {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getHistoryList(
                baseRequest.member_id,
                baseRequest.member_pwd,
                startdate,
                enddate
            )
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }

    // 查詢健身鏡體適能檢測紀錄
    suspend fun getEvaluationLogList(
        startdate: String,
        enddate: String
    ): List<GetEvaluationLogListBeen> {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getEvaluationLogList(
                baseRequest.member_id,
                baseRequest.member_pwd,
                startdate,
                enddate
            )
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }

    // 查詢健身鏡運動紀錄資料
    suspend fun getActivityLogList(
        startdate: String,
        enddate: String
    ): List<GetActivityLogListBeen> {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getActivityLogList(
                baseRequest.member_id,
                baseRequest.member_pwd,
                startdate,
                enddate
            )
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }


    suspend fun userUploadpic(upload_filename: String): BookDataResponse {
        val baseRequest = BaseBookRequest()
        val request = MultipartBody.Builder()
            .apply {
                setType(MultipartBody.FORM)
                addFormDataPart("member_id", baseRequest.member_id)
                addFormDataPart("member_pwd", baseRequest.member_pwd)
                val fileName = upload_filename.substring(
                    upload_filename.lastIndexOf("/") + 1
                )
                val file = File(upload_filename)
                if (file.exists())
                    addFormDataPart(
                        "upload_filename", fileName,
                        toFileRequestBody(file)!!
                    )
            }
        return AppClientManager.instance.bookService.userUploadpic(request.build())
    }

    suspend fun getStationList(): List<StationListData> {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getStationList(
                baseRequest.member_id,
                baseRequest.member_pwd
            )
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }

    suspend fun getStationInfo(sid: String): List<StationInfoData> {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getStationInfo(
                baseRequest.member_id,
                baseRequest.member_pwd,
                sid
            )
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }

    suspend fun getStationService(sid: String): List<StationServiceData> {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getStationService(
                baseRequest.member_id,
                baseRequest.member_pwd,
                sid
            )
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }

    suspend fun getBookingDay(sid: String, reserve_date: String, sc: String): BookingDayListData {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getBookingDay(
                baseRequest.member_id,
                baseRequest.member_pwd,
                sid,
                reserve_date,
                sc
            )
        } catch (e: Exception) {
            e.printStackTrace()
            BookingDayListData()
        }
    }

    suspend fun addBooking(
        sid: String,
        reserve_date: String,
        reserve_time: String,
        service_item: String
    ): AddBookingData {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.addBooking(
                baseRequest.member_id,
                baseRequest.member_pwd,
                sid,
                reserve_date,
                reserve_time,
                service_item
            )
        } catch (e: Exception) {
            e.printStackTrace()
            AddBookingData()
        }
    }

    suspend fun getBookingList(): List<BookingRecordData> {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getBookingList(
                baseRequest.member_id,
                baseRequest.member_pwd
            )
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }

    suspend fun getBookingInfo(booking_no: String): BookingInfoData {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getBookingInfo(
                baseRequest.member_id,
                baseRequest.member_pwd,
                booking_no
            )[0]
        } catch (e: Exception) {
            e.printStackTrace()
            BookingInfoData()
        }
    }

    suspend fun getBookingInfo2(booking_no: String): BookingInfoData {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getBookingInfo2(
                baseRequest.member_id,
                baseRequest.member_pwd,
                booking_no
            )[0]
        } catch (e: Exception) {
            e.printStackTrace()
            BookingInfoData()
        }
    }

    suspend fun outpatient_info(booking_no: String): BookingInfoData {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.outpatient_info(
                baseRequest.member_id,
                baseRequest.member_pwd,
                booking_no
            )[0]
        } catch (e: Exception) {
            // return  Result<BaseBookResponse>.isFailure()
            BookingInfoData()
        }
    }

    suspend fun cancelBooking(booking_no: String): CancelBookingData {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.cancelBooking(
                baseRequest.member_id,
                baseRequest.member_pwd,
                booking_no
            )
        } catch (e: Exception) {
            e.printStackTrace()
            CancelBookingData()
        }
    }

    suspend fun cancelBooking2(booking_no: String): CancelBookingData {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.cancelBooking2(
                baseRequest.member_id,
                baseRequest.member_pwd,
                booking_no
            )
        } catch (e: Exception) {
            e.printStackTrace()
            CancelBookingData()
        }
    }

    suspend fun reschedule(booking_no: String): CancelBookingData {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.reschedule(
                baseRequest.member_id,
                baseRequest.member_pwd,
                booking_no
            )
        } catch (e: Exception) {
            e.printStackTrace()
            CancelBookingData()
        }
    }

    suspend fun updatebooking_status(booking_no: String, no: String): BaseBookResponse {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.updatebooking_status(
                baseRequest.member_id,
                baseRequest.member_pwd,
                booking_no, no
            )
        } catch (e: Exception) {
            e.printStackTrace()
            BaseBookResponse()
        }
    }

    /*2022/04/08新增*/
    suspend fun get_workingday2(sid: String, serCo: String, sD: String): WorkingDay2Data {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.get_workingday2(
                baseRequest.member_id,
                baseRequest.member_pwd,
                sid,
                serCo,
                sD
            )
        } catch (e: Exception) {
            WorkingDay2Data()
        }
    }

    suspend fun get_workingday3(sid: String): WorkingDay2Data {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.get_workingday3(
                baseRequest.member_id,
                baseRequest.member_pwd,
                sid,
            )
        } catch (e: Exception) {
            WorkingDay2Data()
        }
    }

    suspend fun activity_info(aid: String): List<AcInResponse> {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.activity_info(
                baseRequest.member_id,
                baseRequest.member_pwd,
                aid
            )
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    suspend fun getSMList(): List<StationListResponse> {
        return try {
            var baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getSMlist(
                baseRequest.member_id,
                baseRequest.member_pwd
            )
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }

    suspend fun getHospitalList(): List<StationListData> {
        return try {
            var baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getHospitalList(
                baseRequest.member_id,
                baseRequest.member_pwd
            )
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }

    suspend fun getHospitalInfo(sid: String): List<StationInfoData> {
        return try {
            var baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getHospitalInfo(
                baseRequest.member_id,
                baseRequest.member_pwd,
                sid
            )
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }

    suspend fun getDivisionList(sid: String): List<DivisionListData> {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getDivisionList(
                baseRequest.member_id,
                baseRequest.member_pwd,
                sid
            )
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }

    suspend fun getPhysicianList(sid: String, did: String): List<PhysicianListData> {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getPhysicianList(
                baseRequest.member_id,
                baseRequest.member_pwd,
                sid,
                did
            )
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }

    suspend fun getPhysicianWorkingDay(
        sid: String,
        pid: String,
        start_date: String
    ): PhysicianWorkingDayListData {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getPhysicianWorkingDay(
                baseRequest.member_id,
                baseRequest.member_pwd,
                sid,
                pid,
                start_date
            )
        } catch (e: Exception) {
            e.printStackTrace()
            PhysicianWorkingDayListData()
        }
    }

    suspend fun addVideoReserveOrder(
        sid: String, did: String, pid: String,
        reserve_date: String, reserve_time: String, reserve_endtime: String, price: String,
        member_phone: String, member_name: String, member_email: String, question: String
    ): CommonData {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.addVideoReserveOrder(
                baseRequest.member_id, baseRequest.member_pwd, sid, did, pid,
                reserve_date, reserve_time, reserve_endtime, price,
                member_phone, member_name, member_email, question
            )
        } catch (e: Exception) {
            e.printStackTrace()
            CommonData()
        }
    }

    suspend fun videoRecordList(): List<VideoRecordListData> {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.videoRecordList(
                baseRequest.member_id,
                baseRequest.member_pwd
            )
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }

    suspend fun videoRecordCancel(booking_no: String): CommonData {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.videoRecordCancel(
                baseRequest.member_id,
                baseRequest.member_pwd,
                booking_no
            )
        } catch (e: Exception) {
            e.printStackTrace()
            CommonData()
        }
    }

    suspend fun videoRecordOutpatient(booking_no: String): List<VideoRecordOutpatientData> {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.videoRecordOutpatient(
                baseRequest.member_id,
                baseRequest.member_pwd,
                booking_no
            )
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }

    suspend fun addBooking2(
        sid: String, name: String, phone: String,
        pid: String, birth: String, date: String, time: String, pic: File

    ): AddBookingData {
        Log.d("圖檔名", "${pic.name}?}")

        val multipart = MultipartBody.Builder().setType(MultipartBody.FORM)
        SharedPreferencesUtil.instances.getAccountId()
            ?.let { multipart.addFormDataPart("member_id", it) }
        SharedPreferencesUtil.instances.getAccountPwd()
            ?.let { multipart.addFormDataPart("member_pwd", it) }
        multipart.addFormDataPart("sid", sid)
        multipart.addFormDataPart("member_phone", name)
        multipart.addFormDataPart("member_name", phone)
        multipart.addFormDataPart("member_pid", pid)
        multipart.addFormDataPart("member_birthday", birth)
        multipart.addFormDataPart("reserve_date", date)
        multipart.addFormDataPart("reserve_time", time)
        multipart.addFormDataPart(
            name = "upload_filename",
            filename = pic.name,
            body = pic.asRequestBody("image/*".toMediaType())
        )
        return AppClientManager.instance.bookService.addBooking2(multipart.build())
    }

    suspend fun getBookingList2(): List<BookingRecordData> {
        return try {
            var baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getBookingList2(
                baseRequest.member_id,
                baseRequest.member_pwd
            )
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }

    suspend fun upload_messagelog(booking_no: String, txt: String): messagelogData {
        return try {
            var baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.upload_messagelog(
                baseRequest.member_id,
                baseRequest.member_pwd,
                booking_no, txt
            )
        } catch (e: Exception) {
            e.printStackTrace()
            messagelogData()
        }
    }

    suspend fun upload_messagepic(booking_no: String, pic: File): messagelogData {
        val multipart = MultipartBody.Builder().setType(MultipartBody.FORM)
        multipart.addFormDataPart(
            name = "upload_filename",
            filename = pic.name,
            body = pic.asRequestBody("image/*".toMediaType())
        )
        SharedPreferencesUtil.instances.getAccountId()
            ?.let { multipart.addFormDataPart("member_id", it) }
        SharedPreferencesUtil.instances.getAccountPwd()
            ?.let { multipart.addFormDataPart("member_pwd", it) }
        multipart.addFormDataPart("booking_no", booking_no)
        return AppClientManager.instance.bookService.upload_messagepic(multipart.build())
    }


    // 關懷
    suspend fun addCarelist(cId: String): AddCarelistData {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.addCarelist(
                baseRequest.member_id,
                baseRequest.member_pwd,
                cId
            )
        } catch (e: Exception) {
            e.printStackTrace()
            AddCarelistData()
        }
    }

    // null
    suspend fun getCareList(): List<CareListVO> {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getCareList(
                baseRequest.member_id,
                baseRequest.member_pwd
            )
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }

    suspend fun getCaredList(): List<CareListVO> {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getCaredList(
                baseRequest.member_id,
                baseRequest.member_pwd
            )
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }

    suspend fun updateCareNickName(
        nickName: String,
        cId: String
    ): UpdateCareNickName {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.updateCareNickName(
                baseRequest.member_id,
                baseRequest.member_pwd,
                nickName,
                cId
            )
        } catch (e: Exception) {
            e.printStackTrace()
            UpdateCareNickName()
        }
    }

    suspend fun deleteCareMember(cId: String): DeleteCareMember {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.deleteCareMember(
                baseRequest.member_id,
                baseRequest.member_pwd,
                cId
            )
        } catch (e: Exception) {
            e.printStackTrace()
            DeleteCareMember()
        }
    }

    suspend fun cancelCaredMember(cId: String, sms: String): CancelCaredMember {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.cancelCaredMember(
                baseRequest.member_id,
                baseRequest.member_pwd,
                cId,
                sms
            )
        } catch (e: Exception) {
            e.printStackTrace()
            CancelCaredMember()
        }
    }


    suspend fun getMsgBoxList(): List<NotifyHistoryListVO> {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getMsgBoxList(
                baseRequest.member_id,
                baseRequest.member_pwd
            )
        } catch (e: Exception) {
            e.printStackTrace()
            arrayListOf()
        }
    }

    // null
    suspend fun getMessageBoxCount(): MessageBoxCountVO {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.getMessageBoxCount(
                baseRequest.member_id,
                baseRequest.member_pwd
            )
        } catch (e: Exception) {
            e.printStackTrace()
            MessageBoxCountVO()
        }
    }

    suspend fun updateMsgBoxReadStatus(rId: String): MessageBoxStatus {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.updateMessageReadStatus(
                baseRequest.member_id,
                baseRequest.member_pwd,
                rId
            )
        } catch (e: Exception) {
            e.printStackTrace()
            MessageBoxStatus()
        }
    }

    suspend fun authCareStatus(
        status: String,
        messageFrom: String
    ): AuthCareStatus {
        return try {
            val baseRequest = BaseBookRequest()
            AppClientManager.instance.bookService.authCareStatus(
                baseRequest.member_id,
                baseRequest.member_pwd,
                status,
                messageFrom
            )
        } catch (e: Exception) {
            e.printStackTrace()
            AuthCareStatus()
        }
    }
}