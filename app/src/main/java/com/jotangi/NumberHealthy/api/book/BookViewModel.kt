package com.jotangi.NumberHealthy.api.book

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jotangi.NumberHealthy.StoreMangerUi.googlevision.StationListResponse
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.jotangi.NumberHealthy.utils.SingleLiveEvent
import org.json.JSONArray
import java.io.File

class BookViewModel(var bookApiRepository: BookApiRepository) : ViewModel() {

    val playStoreVersion = MediatorLiveData<String>()

    val memberInfoLD = MediatorLiveData<MemberInfoData>()

    val stationListLiveData = MediatorLiveData<List<StationListData>>()
    val stationInfoLiveData = MediatorLiveData<StationInfoData>()
    val stationServiceLiveData = MediatorLiveData<List<StationServiceData>>()
    val bookingDayLiveData = MediatorLiveData<List<BookingDayData>>()
    val addBookingLiveData = MediatorLiveData<AddBookingData>()
    val bookingRecordLiveData = MediatorLiveData<List<BookingRecordData>>()
    val smRecordLiveData = MediatorLiveData<List<StationListResponse>>()

    val bookingInfoLiveData = MediatorLiveData<BookingInfoData>()
    val cancelBookingLiveData = MediatorLiveData<CancelBookingData>()
    val WD2LiveData = MediatorLiveData<List<WorkingDay201Data>>()
    val WD3LiveData = MediatorLiveData<List<WorkingDay201Data>>()
    val AILiveData = MediatorLiveData<List<AcInResponse>>()

    // 醫電
    val hospitalListLiveData = MediatorLiveData<List<StationListData>>()
    val hospitalInfoLiveData = MediatorLiveData<StationInfoData>()
    val hospitalDivisionData = MediatorLiveData<List<DivisionListData>>()
    val hospitalPhysicianData = MediatorLiveData<List<PhysicianListData>>()
    val physicianWorkingDayListData = MediatorLiveData<List<PhysicianResponseData>>()
    val hospitalOrderNumber = MediatorLiveData<String>()
    val videoRecordListData = MediatorLiveData<List<VideoRecordListData>>()
    val videoRecordCancelData = MediatorLiveData<CommonData>()
    val videoRecordOutpatientListData = SingleLiveEvent<List<VideoRecordOutpatientData>>()

    val addBooking2LiveData = MediatorLiveData<AddBookingData>()
    val bookingRecord2LiveData = MediatorLiveData<List<BookingRecordData>>()
    val bookingInfo2LiveData = MediatorLiveData<BookingInfoData>()
    val outpatient_infoLiveData = MediatorLiveData<BookingInfoData>()
    val cancelBooking2LiveData = MediatorLiveData<CancelBookingData>()
    val rescheduleLiveData = MediatorLiveData<CancelBookingData>()
    val updatebooking_statusLiveData = MediatorLiveData<BaseBookResponse>()
    val messagelogLiveData = MediatorLiveData<messagelogData>()

    // 關懷
    val careAuthMessageLiveData = MediatorLiveData<String>()
    val careList = MediatorLiveData<MutableList<CareListVO>?>()
    val caredList = MediatorLiveData<MutableList<CareListVO>?>()
    val careNickNameUpdateResult = MediatorLiveData<String>()
    val careMemberDeleteResult = MediatorLiveData<String>()
    val careMemberCancelResult = MediatorLiveData<String>()
    val msgBoxList = MediatorLiveData<List<NotifyHistoryListVO>>()
    val messageBoxCountResult = MediatorLiveData<String>()

    var isoK = MutableLiveData(false)
    var bmp2 = MutableLiveData<Bitmap>()
    var file2 = MutableLiveData<File>()
    var file3 = MutableLiveData<Uri>()

    // 票券
    val getCouponList = MediatorLiveData<List<GetCouponDataBeen>>()
    val getCouponPoint = MediatorLiveData<String>()

    // 查詢健身鏡體適能檢測紀錄
    val getEvaluationLogList = MediatorLiveData<List<GetEvaluationLogListBeen>>()

    // 查詢健身鏡運動紀錄資料
    val getActivityLogList = MediatorLiveData<List<GetActivityLogListBeen>>()

    //歷史紀錄
    val getHistoryList = MediatorLiveData<List<GetStoreApplyListBeen>>()


    fun setPlayStoreVersion(version: String) {
        if (playStoreVersion.value != version) {
            playStoreVersion.postValue(version)
        }
    }

    // ---------------------------------------- 會員 ---------------------------------------

    suspend fun memberInfo() {
        val data = bookApiRepository.memberInfo()
        memberInfoLD.postValue(data)
    }

    suspend fun getCouponList() {
        val list = bookApiRepository.getCouponList()
        if (list.isNotEmpty()) {
            getCouponList.postValue(list)
        }
    }

    suspend fun getCouponPoint(success: () -> Unit, fail: () -> Unit) {
        val data = bookApiRepository.getCouponPoint()
        if (data.code == "0x0200" && !data.Points.isNullOrBlank()) {
            getCouponPoint.postValue(data.Points!!)
            success()

        } else {

            fail()
        }
    }

    suspend fun getHistoryList(startdate: String, enddate: String) {
        val list = bookApiRepository.getHistoryList(startdate, enddate)
        if (list.isNotEmpty()) {
            getHistoryList.postValue(list)
        }
    }

    // 查詢健身鏡體適能檢測紀錄
    suspend fun getEvaluationLogList(startdate: String, enddate: String) {
        val list = bookApiRepository.getEvaluationLogList(startdate, enddate)
        if (list.isNotEmpty()) {
            getEvaluationLogList.postValue(list)
        }
    }

    // 查詢健身鏡運動紀錄資料
    suspend fun getActivityLogList(startdate: String, enddate: String) {
        val list = bookApiRepository.getActivityLogList(startdate, enddate)
        if (list.isNotEmpty()) {
            getActivityLogList.postValue(list)
        }
    }


    // 足壓量測
    val fpmList = MutableLiveData<JSONArray>()

    fun setFpmList(list: JSONArray) {
        fpmList.value = list
    }

    fun setok() {
        isoK.value = true
    }

    fun setnotok() {
        isoK.value = false
    }

    fun setbmp(bmp: Bitmap) {
        bmp2.value = bmp
    }

    fun setfile(file: File) {
        file2.value = file
    }

    fun seturi(file: Uri) {
        file3.value = file
    }

    suspend fun getStationList() {
        val data = bookApiRepository.getStationList()
        if (data.isNotEmpty()) {
            stationListLiveData.postValue(data)
        }
    }

    suspend fun getStationInfo(sid: String) {
        val data = bookApiRepository.getStationInfo(sid)
        if (data.isNotEmpty()) {
            stationInfoLiveData.postValue(data[0])
        }
    }

    suspend fun getStationService(sid: String) {
        val data = bookApiRepository.getStationService(sid)
        if (data.isNotEmpty()) {
            stationServiceLiveData.postValue(data)
        }
    }

    suspend fun getBookingDay(sid: String, reserve_date: String, sc: String) {
        val response = bookApiRepository.getBookingDay(sid, reserve_date, sc)
        val data = response.responseMessage
        var code = response.code
        bookingDayLiveData.postValue(data!!)
    }

    suspend fun addBooking(
        sid: String,
        reserve_date: String,
        reserve_time: String,
        service_item: String
    ) {
        val data = bookApiRepository.addBooking(sid, reserve_date, reserve_time, service_item)
        addBookingLiveData.postValue(data)
    }

    suspend fun getBookingRecord() {
        val data = bookApiRepository.getBookingList()
        if (data.isNotEmpty()) {
            bookingRecordLiveData.postValue(data)
        }
    }

    suspend fun getBookingInfo(booking_no: String) {
        val data = bookApiRepository.getBookingInfo(booking_no)
        bookingInfoLiveData.postValue(data)
    }

    suspend fun getBookingInfo2(booking_no: String) {
        val data = bookApiRepository.getBookingInfo2(booking_no)
        bookingInfo2LiveData.postValue(data)
    }

    suspend fun outpatient_info(booking_no: String) {
        val data = bookApiRepository.outpatient_info(booking_no)
        outpatient_infoLiveData.postValue(data)
    }

    suspend fun cancelBooking(booking_no: String) {
        val data = bookApiRepository.cancelBooking(booking_no)
        cancelBookingLiveData.postValue(data)
    }

    suspend fun cancelBooking2(booking_no: String) {
        val data = bookApiRepository.cancelBooking2(booking_no)
        cancelBooking2LiveData.postValue(data)
    }

    suspend fun reschedule(booking_no: String) {
        val data = bookApiRepository.reschedule(booking_no)
        rescheduleLiveData.postValue(data)
    }

    suspend fun updatebooking_status(booking_no: String, no: String) {
        val data = bookApiRepository.updatebooking_status(booking_no, no)
        updatebooking_statusLiveData.postValue(data)
    }

    suspend fun getWD2(sid: String, serCo: String, sD: String) {

        val response = bookApiRepository.get_workingday2(sid, serCo, sD)
        val data = response.responseMessage
        WD2LiveData.postValue(data!!)
    }

    suspend fun getWD3(sid: String) {

        val response = bookApiRepository.get_workingday3(sid)
        val data = response.responseMessage
        WD3LiveData.postValue(data!!)
    }

    suspend fun getAI(aid: String) {

        val data = bookApiRepository.activity_info(aid)

        if (data.isNotEmpty()) {
            AILiveData.postValue(data)
        }
    }

    suspend fun getsmRecord() {
        val data = bookApiRepository.getSMList()
        if (data.isNotEmpty()) {
            smRecordLiveData.postValue(data)
        }
    }

    suspend fun getHospitalList() {
        val data = bookApiRepository.getHospitalList()
        if (data.isNotEmpty()) {
            hospitalListLiveData.postValue(data)
        }
    }

    suspend fun getHospitalInfo(sid: String) {
        val data = bookApiRepository.getHospitalInfo(sid)
        if (data.isNotEmpty()) {
            hospitalInfoLiveData.postValue(data[0])
        }
    }

    suspend fun getDivisionList(sid: String) {
        val data = bookApiRepository.getDivisionList(sid)
        if (data.isNotEmpty()) {
            hospitalDivisionData.postValue(data)
        }
    }

    suspend fun getPhysicianList(sid: String, did: String) {
        val data = bookApiRepository.getPhysicianList(sid, did)
        if (data.isNotEmpty()) {
            hospitalPhysicianData.postValue(data)
        }
    }

    suspend fun getPhysicianWorkingDay(sid: String, pid: String, start_date: String) {
        val response = bookApiRepository.getPhysicianWorkingDay(sid, pid, start_date)
        response.responseMessage.let {
            physicianWorkingDayListData.postValue(it)
        }
    }

    suspend fun addVideoReserveOrder(
        sid: String, did: String, pid: String,
        reserve_date: String, reserve_time: String, reserve_endtime: String, price: String,
        member_phone: String, member_name: String, member_email: String, question: String
    ) {
        val response = bookApiRepository.addVideoReserveOrder(
            sid, did, pid,
            reserve_date, reserve_time, reserve_endtime, price,
            member_phone, member_name, member_email, question
        )

        if (response.code == "0x0200") {
            response.responseMessage.let {
                hospitalOrderNumber.postValue(it)
            }
        }
    }

    // 視訊預約紀錄
    suspend fun videoRecordList() {
        val list = bookApiRepository.videoRecordList()
        if (list.isNotEmpty()) {
            videoRecordListData.postValue(list)
        }
    }

    // (62)	Tours app 會員取消視訊諮詢預約資料
    suspend fun videoRecordCancel(booking_no: String) {
        val response = bookApiRepository.videoRecordCancel(booking_no)
        videoRecordCancelData.postValue(response)
    }

    // (63)	Tours app查詢視訊諮詢診次資料
    suspend fun videoRecordOutpatient(booking_no: String) {
        val list = bookApiRepository.videoRecordOutpatient(booking_no)
        if (list.isNotEmpty()) {
            videoRecordOutpatientListData.postValue(list)
        }
    }

    suspend fun addBooking2(
        sid: String,
        name: String,
        phone: String,
        pid: String, bith: String, date: String, time: String, file: File
    ) {
        val data = bookApiRepository.addBooking2(sid, name, phone, pid, bith, date, time, file)
        addBooking2LiveData.postValue(data)
    }

    suspend fun getBookingRecord2() {
        val data = bookApiRepository.getBookingList2()
        if (data.isNotEmpty()) {
            bookingRecord2LiveData.postValue(data)
        }
    }

    suspend fun upload_messagelog(booking_no: String, txt: String) {
        val data = bookApiRepository.upload_messagelog(booking_no, txt)
        messagelogLiveData.postValue(data)
    }

    suspend fun upload_messagepic(booking_no: String, txt: File) {
        val data = bookApiRepository.upload_messagepic(booking_no, txt)
        messagelogLiveData.postValue(data)
    }


    // 關懷
    suspend fun getCareList() {
        val data = bookApiRepository.getCareList()
        if (data.isNotEmpty()) {
            careList.postValue(data as MutableList<CareListVO>?)
        } else {
            careList.postValue(null)
        }
    }

    suspend fun getCaredList() {
        val data = bookApiRepository.getCaredList()
        if (data.isNotEmpty()) {
            caredList.postValue(data as MutableList<CareListVO>?)
        } else {
            caredList.postValue(null)
        }
    }

    suspend fun addCarelist(cmemberId: String) {
        val response = bookApiRepository.addCarelist(cmemberId)
        val responseMessage = response.responseMessage
        careAuthMessageLiveData.postValue(responseMessage)
    }


    suspend fun updateCareNickName(
        nickName: String,
        cId: String
    ) {
        val response = bookApiRepository.updateCareNickName(
            nickName,
            cId
        )
        val code = response.code
        careNickNameUpdateResult.postValue(code!!)
    }

    suspend fun deleteCareMember(cId: String) {
        val response = bookApiRepository.deleteCareMember(cId)
        val code = response.code
        careMemberDeleteResult.postValue(code!!)
    }

    suspend fun cancelCaredMember(cId: String, sms: String) {
        val response = bookApiRepository.cancelCaredMember(cId, sms)
        val code = response.code
        careMemberCancelResult.postValue(code!!)
    }

    suspend fun getMsgBoxList() {
        val data = bookApiRepository.getMsgBoxList()
        if (data.isNotEmpty()) {
            msgBoxList.postValue(data)
        }
    }

    suspend fun getMessageBoxCount() {
        val response = bookApiRepository.getMessageBoxCount()
        val responseMessage = response.responseMessage
        messageBoxCountResult.postValue(responseMessage)
    }

    suspend fun updateMessageBoxReadStatus(rId: String) {
        bookApiRepository.updateMsgBoxReadStatus(rId)
    }

    fun clearData() {
        stationListLiveData.postValue(listOf())
        careAuthMessageLiveData.postValue("")
        careList.postValue(mutableListOf())
        caredList.postValue(mutableListOf())
        careNickNameUpdateResult.postValue("")
        careMemberDeleteResult.postValue("")
        careMemberCancelResult.postValue("")
        msgBoxList.postValue(listOf())
        messageBoxCountResult.postValue("")
        getEvaluationLogList.postValue(mutableListOf())
        getActivityLogList.postValue(mutableListOf())
        getCouponPoint.postValue("")

    }
}