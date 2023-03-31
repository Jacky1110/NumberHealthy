package com.jotangi.NumberHealthy.utils

import android.app.Notification
import android.os.Bundle
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil.Companion.instances
import com.yucheng.ycbtsdk.YCBTClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class NotifyListener : NotificationListenerService() {

    private val TAG = "${javaClass.simpleName}(TAG)"

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        if (sbn!!.notification.flags and Notification.FLAG_GROUP_SUMMARY != 0) {
            //Ignore the notification
            return
        }

        val bundle = sbn!!.notification.extras
//        Log.d(TAG, "bundle: $bundle")
        val packageName = sbn!!.packageName
        var title = normalStr(getTitle(bundle))
        var text = normalStr(getText(bundle))
//        Log.d(TAG, "packageName: $packageName title:[${title}] text: [${text}]")

        CoroutineScope(Dispatchers.IO).launch {

            if (checkNotify(packageName) || checkNotify(title) || checkNotify(text)) {

                if (title.length < 3) {
                    title += "   "
                }
                if (text.length < 3) {
                    text += "   "
                }

                Log.d(TAG,"[send]title: $title text: $text")
                // title、text 長度至少要 3
                YCBTClient.appSengMessageToDevice(
                    0x03, title, text
                ) { code, ratio, hashMap -> Log.w(TAG, "onDataResponse: $code") }
            }
        }
    }

    /**
     * 8206、8234、8236
     */
    private fun normalStr(str: String): String {

        var real = ""
        var code: Int
        val removeList = listOf(8206, 8234, 8236)

        tag@ for (i in str.indices) {

            code = str[i].code
            for (j in removeList.indices) {
                if (code == removeList[j]) {
                    continue@tag
                }
            }

            real += str[i]
        }

        return real
    }

    private fun checkNotify(data: String): Boolean {
        return instances.checkNotify(data)
    }

    private fun getTitle(bundle: Bundle): String {
        var title = bundle.getString(Notification.EXTRA_TITLE, "")
        if (title.isEmpty()) {
            title = "..."
            val list = bundle.toString().split(", ").toTypedArray()
            for (s in list) {
                if (s.contains(Notification.EXTRA_TITLE) && !s.contains("null")) {
                    title = s.substring(s.indexOf("=") + 1)
                    break
                }
            }
        }
        return title
    }

    private fun getText(bundle: Bundle): String {
        var text = bundle.getString(Notification.EXTRA_TEXT, "")
        if (text.isEmpty()) {
            text = "..."
            val list = bundle.toString().split(", ").toTypedArray()
            for (s in list) {
                if (s.contains(Notification.EXTRA_TEXT) && !s.contains("null")) {
                    text = s.substring(s.indexOf("=") + 1)
                    break
                }
            }
        }
        return text
    }
}