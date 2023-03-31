package com.jotangi.NumberHealthy.chart

import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jotangi.NumberHealthy.api.watch.TemperatureBean
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.constant.BcWith
import com.jotangi.NumberHealthy.utils.constant.BodyTemperatureColor
import com.jotangi.NumberHealthy.utils.constant.ChartTextSize
import com.jotangi.NumberHealthy.utils.constant.InitValue

class BarBodyTemperature private constructor() {

    private val TAG = "${javaClass.simpleName}(TAG)"

    companion object {
        val instance by lazy { BarBodyTemperature() }
    }

    fun init(chart: BarChart) {

        chart.apply {

            setTouchEnabled(false)
            legend.isEnabled = false
            description.isEnabled = false

            with(xAxis) {

                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
                textSize = ChartTextSize.AxisX.value

                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return ""
                    }
                }
            }

            with(axisLeft) {
                axisMaximum = 45F
                axisMinimum = 30F
                setDrawLabels(false)
            }

            with(axisRight) {
                axisMaximum = 45F
                axisMinimum = 30F
                textSize = ChartTextSize.AxisR.value

                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "${value.toInt()}°c"
                    }
                }
            }

            val entries: ArrayList<BarEntry> = ArrayList()
            for (i in 0..6) {
                entries.add(BarEntry(i.toFloat(), 0F))
            }

            data = BarData(BarDataSet(entries, ""))
            invalidate()
        }
    }

    fun setValue(list: List<TemperatureBean>, chart: BarChart) {

        val last7Data = list.take(7)
        val entries: ArrayList<BarEntry> = ArrayList()
        for (i in 0..6) {
            entries.add(BarEntry(i.toFloat(), InitValue.Entries.value))
        }
        for (i in last7Data.indices) {
            entries[6 - i] = BarEntry((6 - i).toFloat(), last7Data[i].temperature.toFloat())
        }

        chart.apply {

            val barDataSet = BarDataSet(entries, "")
            with(barDataSet) {
                valueTextSize = ChartTextSize.Cost.value
                color = Color.parseColor(BodyTemperatureColor.Theme.value)
            }

            val barData = BarData(barDataSet)

            with(barData) {

                barWidth = BcWith.Size.value

                setValueFormatter(object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "$value"
                    }
                })
            }

            with(xAxis) {

                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return try {
                            DateUtil.instance.clipTimeToHm(
                                last7Data[6 - value.toInt()].startTime!!
                            )
                        } catch (e: Exception) {
                            ""
                        }
                    }
                }
            }

            data = barData
            invalidate()
        }
    }
}