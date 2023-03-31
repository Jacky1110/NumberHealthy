package com.jotangi.NumberHealthy.ui.mylittlemin

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoreData(
    var title: String = "",
    var imgUrl: String = "",
    var broadcastContent: String = "",
    var address: String = "",
    var date: String = "",
    var phone: String = "",
    var detail: String = "",
    var item: String = ""
) : Parcelable