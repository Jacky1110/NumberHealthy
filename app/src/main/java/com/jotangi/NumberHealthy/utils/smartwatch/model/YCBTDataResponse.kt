package com.jotangi.NumberHealthy.utils.smartwatch.model

class YCBTDataResponse {
    var code = 0
    var dataType = 0
    var data: List<Any?>? = null

    constructor() {}
    constructor(code: Int, dataType: Int, data: List<Any?>?) {
        this.code = code
        this.dataType = dataType
        this.data = data
    }

    companion object {
        const val KEY_CODE = "code"
        const val KEY_DATA_TYPE = "dataType"
        const val KEY_DATA = "data"
    }
}