package com.jotangi.NumberHealthy.ui.mylittlemin

import android.os.Parcelable
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import kotlinx.android.parcel.Parcelize


/**
 * (1)	Tours app會員註冊
 */
@Parcelize
data class UserRegister(
    var memberId: String = "",
    var memberPwd: String = "",
    var memberName: String = "",
    var memberType: String = ""
) : Parcelable

/**
 * (2)	Tours app會員登入
 */
@Parcelize
data class UserLoginRequest(
    var memberId: String = "",
    var memberPwd: String = "",
    var fcmToken: String?,
    var unique_id: String?
) : Parcelable

/**
 * (3)	Tours app會員忘記密碼
 */
@Parcelize
data class UserResetPwd(
    var memberId: String = "",
    var memberPwd: String = "",
    var code: String = "",
) : Parcelable

/**
 * (8)	Tours app會員資料異動
 */
@Parcelize
data class UserEditRequest(
    var name: String = "",
    var gender: String = "",
    var email: String = "",
    var birthday: String = "",
    var address: String = "",
    var phone: String?
) : Parcelable


@Parcelize
data class BaseBookRequest(
    var member_id: String = SharedPreferencesUtil.instances.getAccountId()!!,
    var member_pwd: String = SharedPreferencesUtil.instances.getAccountPwd()!!
) : Parcelable