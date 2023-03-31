package com.jotangi.NumberHealthy.utils.constant

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 九宮格
 */
enum class JiugonggeEnum {
    LEGEND, BACKGROUND, ICON
}

@Parcelize
data class MacularLineData(
    var LeftList: MutableList<Float> = MutableList(7) { MacularElement.Error.value },
    var RightList: MutableList<Float> = MutableList(7) { MacularElement.Error.value },
    var DateList: MutableList<String> = MutableList(7) { "" },
) : Parcelable