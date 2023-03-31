package com.jotangi.NumberHealthy.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import timber.log.Timber

/**
 *Created by Luke Liu on 2021/8/19.
 */

class SharedPreferencesUtil private constructor() {

    private val TAG: String = "${javaClass.simpleName}(TAG)"

    private lateinit var spInit: SharedPreferences
    private lateinit var spMember: SharedPreferences
    private lateinit var spWatch: SharedPreferences
    private lateinit var spNotify: SharedPreferences
    private lateinit var spFpm: SharedPreferences


    companion object {
        val instances: SharedPreferencesUtil = SharedPreferencesUtil()
    }

    fun init(context: Context) {
        spInit = context.getSharedPreferences("appInit", Context.MODE_PRIVATE)
        spMember = context.getSharedPreferences("memberInfo", Context.MODE_PRIVATE)
        spWatch = context.getSharedPreferences("watch", Context.MODE_PRIVATE)
        spNotify = context.getSharedPreferences("notify", Context.MODE_PRIVATE)
        spFpm = context.getSharedPreferences("fpm", Context.MODE_PRIVATE)
    }

    private val KEY_APP_VERSION = "key_app_version"
    private val KEY_ACCOUNT_ID = "key_account_id"
    private val KEY_ACCOUNT_MID = "key_account_mid"
    private val KEY_ACCOUNT_PWD = "key_account_pwd"
    private val KEY_HEADSHOT = "key_headshot"
    private val KEY_MEMBER_CODE = "key_member_code"
    private val KEY_WATCH_MAC = "key_watch_mac"
    private val KEY_WATCH_WEAR = "key_watch_wear"
    private val KEY_WATCH_NAME = "key_watch_name"
    val KEY_LAST_UPLOADD_HR_TIME = "key_lut_hr"
    val KEY_LAST_UPLOADD_SLEEP_TIME = "key_lut_sleep"
    val KEY_LAST_UPLOADD_BP_TIME = "key_lut_bp"
    val KEY_LAST_UPLOADD_OXYGEN_TIME = "key_lut_oxygen"
    val KEY_LAST_UPLOADD_TEMPERATURE_TIME = "key_lut_temperature"
    val KEY_LAST_UPLOADD_RESPIRATORY_RATE_TIME = "key_lut_respiratoryRate"
    val KEY_LAST_UPLOADD_STEP_TIME = "key_lut_step"
    val KEY_WATCH_TIME_FORMAT = "key_watch_time_format"
    val KEY_WATCH_FREQUENCY = "KEY_WATCH_FREQUENCY"

    private val KEY_ADMIN_AREA = "KEY_ADMIN_AREA"

    /**
     * Init
     */
    fun setAppVersion(version: String) {
        spInit.edit().putString(KEY_APP_VERSION, version).apply()
    }

    fun getAppVersion(): String? {
        return spInit.getString(KEY_APP_VERSION, null)
    }

    /**
     * Member
     */
    fun setAdminArea(adminArea: String) {
        spWatch.edit().putString(KEY_ADMIN_AREA, adminArea).apply()
    }

    fun getAdminArea(): String? {
        var adminArea = spWatch.getString(KEY_ADMIN_AREA, null)
        return adminArea
    }

    fun setAccountId(id: String?) {
        Timber.e("SharedPreferencesUtil -> setAccountId(id: $id)")
        spMember.edit().putString(KEY_ACCOUNT_ID, id).apply()
    }

    fun getAccountId(): String? {
        val id = spMember.getString(KEY_ACCOUNT_ID, null)
        Timber.e("SharedPreferencesUtil -> getAccountId(id: $id)")
        return id
    }

    fun setAccountPwd(pwd: String?) {
        Timber.e("SharedPreferencesUtil -> setAccountPwd(pwd: $pwd)")
        spMember.edit().putString(KEY_ACCOUNT_PWD, pwd).apply()
    }

    fun getAccountPwd(): String? {
        val pwd = spMember.getString(KEY_ACCOUNT_PWD, null)
        Timber.e("SharedPreferencesUtil -> getAccountPwd(pwd: $pwd)")
        return pwd
    }

    fun setAccountMid(id: String?) {
        spMember.edit().putString(KEY_ACCOUNT_MID, id).apply()
    }

    fun getAccountMid(): String? {
        return spMember.getString(KEY_ACCOUNT_MID, null)
    }

    private fun getAccountHeadShotKey(): String {
        return "${KEY_HEADSHOT}_${getAccountId()}"
    }

    fun setAccountHeadShot(path: String? = null) {
        Timber.e("SharedPreferencesUtil -> setAccountHeadShot(Path: $path)")
        spMember.edit().putString(getAccountHeadShotKey(), path).apply()
    }

    fun getAccountHeadShot(): String? {
        val path = spMember.getString(getAccountHeadShotKey(), null)
        Timber.e("SharedPreferencesUtil -> getAccountHeadShot(path: $path)")
        return path
    }

    fun setMemberCode(code: String?) {
        Timber.e("SharedPreferencesUtil -> setMemberCode(id: $code)")
        code?.let { spMember.edit().putString(KEY_MEMBER_CODE, it).apply() }
    }

    fun getMemberCode(): String? {
        val code = spMember.getString(KEY_MEMBER_CODE, null)
        Timber.e("SharedPreferencesUtil -> getMemberCode(id: $code)")
        return code
    }

    fun setWatchMac(mac: String?) {
        spWatch.edit().putString(KEY_WATCH_MAC, mac).apply()
    }

    fun getWatchMac(): String? {
        val mac = spWatch.getString(KEY_WATCH_MAC, null)
        return mac
    }

    fun setWatchWear(isLeft: Boolean) {
        spWatch.edit().putBoolean(KEY_WATCH_WEAR, isLeft).apply()
    }

    fun getWatchWear(): Boolean {
        val wear = spWatch.getBoolean(KEY_WATCH_WEAR, false)
        return wear
    }

    fun setWatchName(name: String?) {
        spWatch.edit().putString(KEY_WATCH_NAME, name).apply()
    }

    fun getWatchName(): String? {
        val name = spWatch.getString(KEY_WATCH_NAME, null)
        return name
    }

    fun setWatchLongValue(key: String, value: Long) {
        spWatch.edit().putLong(key, value).commit()
    }

    fun getWatchLongValue(key: String): Long {
        val value = spWatch.getLong(key, 0)
        return value
    }

    fun setWatchTimeFormat(format: String) {
        spWatch.edit().putString(KEY_WATCH_TIME_FORMAT, format).apply()
    }

    fun getWatchTimeFormat(): String {
        val format = spWatch.getString(KEY_WATCH_TIME_FORMAT, "24")
        return format!!
    }

    fun setWatchFrequency(frequency: String) {
        spWatch.edit().putString(KEY_WATCH_FREQUENCY, frequency).apply()
    }

    fun getWatchFrequency(): String {
        return spWatch.getString(KEY_WATCH_FREQUENCY, "10分鐘")!!
    }

    private var KA: String = ""


    /*店長功能判斷barcode*/

    fun setSB(KA: String) {
        this.KA = KA
    }

    fun getSB(): String? {
        return KA
    }

    fun getNotifyStatus(key: String): Boolean {
        return spNotify.getBoolean(key, false)
    }

    fun setNotifyStatus(key: String, value: Boolean) {
        spNotify.edit().putBoolean(key, value).apply()
    }

    fun checkNotify(key: String): Boolean {
        val data = spNotify.all
        for (inkey in data.keys) {
            if (key.toLowerCase().contains(inkey)) {
                Log.d(TAG, "key: $inkey")
                Log.d(TAG, "value: ${getNotifyStatus(inkey)}")
                return getNotifyStatus(inkey)
            }
        }
        return false
    }
}