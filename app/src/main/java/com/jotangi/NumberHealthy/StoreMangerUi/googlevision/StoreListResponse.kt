package com.jotangi.NumberHealthy.StoreMangerUi.googlevision

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StationListResponse(
    var rid: String? = null,
    var booking_no: String? = null,
    var store_id: String? = null,
    var reserve_date: String? = null,
    var reserve_time: String? = null,
    var mid: String? = null,
    var member_id: String? = null,
    var member_name: String? = null,
    var service_item: String? = null,
    var service_name: String? = null,
    var reserve_status: String? = null,
    var reserve_created_at: String? = null
) : Parcelable
