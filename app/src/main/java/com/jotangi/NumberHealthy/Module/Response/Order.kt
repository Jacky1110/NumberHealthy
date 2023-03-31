package com.jotangi.NumberHealthy.Module.Response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order (
    var oid: String? = null,
    var order_no: String? = null,
    var order_date: String? = null,
    var store_id: String? = null,
    var member_id: String? = null,
    var order_amount: String? = null,
    var coupon_no: String? = null,
    var discount_amount: String? = null,
    var pay_type: String? = null,
    var order_pay: String? = null,
    var pay_status: String? = null,
    var bonus_point: String? = null,
    var order_status: String? = null,
    var store_name: String? = null,
): Parcelable
