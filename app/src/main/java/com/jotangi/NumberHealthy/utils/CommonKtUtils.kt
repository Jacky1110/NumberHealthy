package com.jotangi.NumberHealthy.utils

class CommonKtUtils private constructor() {

    companion object {
        val instance by lazy { CommonKtUtils() }
    }

    fun checkVersion(version: String, needUpData: (Boolean) -> Unit) {

        SharedPreferencesUtil.instances.getAppVersion()?.let {

            if (version.toInt() > it.toInt()) {
                needUpData(true)
            }
        }
    }
}