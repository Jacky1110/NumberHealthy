package com.jotangi.NumberHealthy.utils

import com.jotangi.NumberHealthy.api.watch.SleepDayTotalData
import com.jotangi.NumberHealthy.api.watch.SleepLineData
import com.jotangi.NumberHealthy.utils.constant.DateType
import com.jotangi.NumberHealthy.utils.constant.SleepElement
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DateUtil private constructor() {

    private val TAG = "${javaClass.simpleName}(TAG)"

    companion object {
        val instance by lazy { DateUtil() }
    }


    /**
     * 取得時間字串
     */
    fun currentYmdHms(): String {
        val sdf = SimpleDateFormat(DateType.YMD_HMS.value, Locale.getDefault())
        return sdf.format(Date())
    }

    fun currentYmdHm(): String {
        val sdf = SimpleDateFormat(DateType.YMD_HM.value, Locale.getDefault())
        return sdf.format(Date())
    }

    fun currentYmd(): String {
        val sdf = SimpleDateFormat(DateType.YMD.value, Locale.getDefault())
        return sdf.format(Date())
    }

    fun current7Ymd(): List<String> {

        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat(DateType.YMD.value, Locale.getDefault())
        val arrayList = ArrayList<String>()
        for (i in 0..6) {
            arrayList.add(sdf.format(calendar.time))
            calendar.add(Calendar.DATE, -1)
        }
        return arrayList.reversed().toList()
    }

    fun current7Md(): List<String> {

        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat(DateType.MD.value, Locale.getDefault())
        val arrayList = ArrayList<String>()
        for (i in 0..6) {
            arrayList.add(sdf.format(calendar.time))
            calendar.add(Calendar.DATE, -1)
        }
        return arrayList.reversed()
    }

    // 睡眠線圖的下方日期
    fun processTrendData(): LinkedList<String> {

        val listDate = LinkedList<String>()

        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat(DateType.MD.value, Locale.getDefault())

        for (i in 0 until 7) {
            listDate.addFirst(sdf.format(calendar.time))
            calendar.add(Calendar.DATE, -1)
        }
        return listDate
    }


    /**
     * 修正時間規格
     */
    fun clipTimeFormatSecond(time: String?): String {
        try {
            time?.let {
                val sdf = SimpleDateFormat(DateType.YMD_HMS.value, Locale.getDefault())
                val sdf2 = SimpleDateFormat(DateType.YMD_HM.value, Locale.getDefault())
                val t1 = sdf.parse(time)
                t1?.let {
                    return sdf2.format(t1)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "-"
    }

    fun clipTimeToYMD(time: String?): String {
        try {
            time?.let {
                val sdf = SimpleDateFormat(DateType.YMD_HMS.value, Locale.getDefault())
                val sdf2 = SimpleDateFormat(DateType.YMD.value, Locale.getDefault())
                val t1 = sdf.parse(time)
                t1?.let {
                    return sdf2.format(t1)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "-"
    }

    fun clipTimeToMd(time: String?): String {
        try {
            time?.let {
                val sdf = SimpleDateFormat(DateType.YMD_HMS.value, Locale.getDefault())
                val sdf2 = SimpleDateFormat(DateType.MD.value, Locale.getDefault())
                val t1 = sdf.parse(time)
                t1?.let {
                    return sdf2.format(t1)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "-"
    }

    fun clipTimeToHm(time: String): String {
        return try {
            val sdf = SimpleDateFormat(DateType.YMD_HMS.value, Locale.getDefault())
            val s = SimpleDateFormat(DateType.HM.value, Locale.getDefault())
            s.format(sdf.parse(time))
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun fillSecond(date: String): String {
        return try {
            val format = SimpleDateFormat(DateType.YMD_HM.value, Locale.getDefault())
            val s = SimpleDateFormat(DateType.YMD_HMS.value, Locale.getDefault())
            s.format(format.parse(date))
        }catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun clipYmdToMd(date: String?): String {
        try {
            date?.let {
                val sdf = SimpleDateFormat(DateType.YMD.value, Locale.getDefault())
                val sdf2 = SimpleDateFormat(DateType.MD.value, Locale.getDefault())
                val t1 = sdf.parse(date)
                t1?.let {
                    return sdf2.format(t1)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "-"
    }

    /**
     * 轉換
     */
    fun ymdhmToCalendar(date: String): Calendar {
        return try {
            val format = SimpleDateFormat(DateType.YMD_HM.value, Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.time = format.parse(date) ?: Date()
            calendar
        } catch (e: Exception) {
            e.printStackTrace()
            Calendar.getInstance()
        }
    }

    /**
     * 睡眠頁
     */
    private fun editBaseTime(data: String): String {
        return "${data.split(" ")[0]} ${SleepElement.BaseTime.value}"
    }

    fun calculationBaseDate(data: String): String {

        var baseDate = editBaseTime(data)

        if (data < baseDate) {

            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat(DateType.YMD_HMS.value, Locale.getDefault())
            calendar.time = sdf.parse(baseDate)
            calendar.add(Calendar.DATE, -1)
            baseDate = sdf.format(calendar.time)
        }

        return baseDate
    }

    private fun isSleepOneDay(calendar: Calendar, day: String): Boolean {

        val c = Calendar.getInstance()
        c.time = calendar.time

        val sdf = SimpleDateFormat(DateType.YMD_HMS.value, Locale.getDefault())
        val endTime = editBaseTime(sdf.format(c.time))
        c.add(Calendar.DATE, -1)
        val startTime = editBaseTime(sdf.format(c.time))
        return startTime < day && day < endTime
    }

    fun sleepLineList(list: List<SleepDayTotalData>): SleepLineData {

        val sevenList = list.take(7)
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -7)
        var data: SleepDayTotalData
        val deepArray = IntArray(7)
        val totalArray = IntArray(7)

        for (i in 0..6) {

            calendar.add(Calendar.DATE, 1)

            for (j in sevenList.indices) {

                data = sevenList[j]

                if (isSleepOneDay(calendar, data.startTime.toString())) {

                    deepArray[i] = data.deepMinuteTotal
                    totalArray[i] = (data.deepMinuteTotal + data.lightMinuteTotal)
                    break
                }
            }
        }

        return SleepLineData(deepArray.toList(), totalArray.toList())
    }


    /**
     * 區間時間 - 總單位
     */
    fun intervalSecond(startTime: String, endTime: String): Long {

        val sdf = SimpleDateFormat(DateType.YMD_HMS.value, Locale.getDefault())
        return try {
            TimeUnit.MILLISECONDS.toSeconds(
                sdf.parse(endTime)!!.time - sdf.parse(startTime)!!.time
            )
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    fun intervalMinute(startTime: String?, endTime: String?): Long {

        if (startTime == null || endTime == null) return 0

        val sdf = SimpleDateFormat(DateType.YMD_HMS.value, Locale.getDefault())
        return try {
            TimeUnit.MILLISECONDS.toMinutes(
                sdf.parse(endTime)!!.time - sdf.parse(startTime)!!.time
            )
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }


    /**
     * 其他
     */
    fun fixTimeLength(str: String): String {
        return if (str.length == 1) "0$str" else str
    }

    fun ago3MonthYmdHms(): String {

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, -3)
        val sdf = SimpleDateFormat(DateType.YMD_HMS.value, Locale.getDefault())
        return sdf.format(calendar.time)
    }

    fun after3MinuteYmdHms(): String {

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 3)
        val sdf = SimpleDateFormat(DateType.YMD_HMS.value, Locale.getDefault())
        return sdf.format(calendar.time)
    }
}