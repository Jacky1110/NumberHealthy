package com.jotangi.NumberHealthy.ui.mylittlemin

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TimeData(
    var date: String? = "",
    var time: String = "",
    var status: Boolean = true
) : Parcelable