package com.jotangi.NumberHealthy.ui.ecg

interface EcgDataCallback {
    interface Start {
        fun receiveECG(value: Float)
        fun receiveHRV(value: Int)
        fun receiveBlood(heartValue: Int, bloodDBP: Int, bloodSBP: Int)
        fun receiveBadSignal()
    }

    interface End {
        fun receive(value: Float)
    }
}