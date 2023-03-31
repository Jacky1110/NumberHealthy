package com.jotangi.NumberHealthy.Module

import android.app.Application
import com.jotangi.NumberHealthy.api.watch.EcgBean

class GlobalVariable : Application() {

    companion object {
        private lateinit var ecgBean: EcgBean
        fun setEcgData(ecgBean: EcgBean) {
            this.ecgBean = ecgBean
        }

        fun getEcgData(): EcgBean {
            return ecgBean
        }


        private var fpmPosition: Int = 0
        fun setFpmPosition(position: Int) {
            this.fpmPosition = position
        }

        fun getFpmPosition(): Int {
            return fpmPosition
        }
    }
}