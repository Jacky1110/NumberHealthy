package com.jotangi.NumberHealthy.chart

import android.content.Context
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.api.watch.BPBean
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.constant.*
import java.math.BigDecimal

class BarBloodPressure private constructor() {

    private val TAG = "${javaClass.simpleName}(TAG)"

    companion object {
        val instance by lazy { BarBloodPressure() }
    }

    fun init(chart: BarChart) {

        chart.apply {

            setTouchEnabled(false)
            legend.isEnabled = false
            description.isEnabled = false

            with(xAxis) {
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTH_SIDED
                textSize = ChartTextSize.AxisX.value

                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return ""
                    }
                }
            }

            setXAxisRenderer(object : XAxisRenderer(
                viewPortHandler,
                xAxis,
                getTransformer(YAxis.AxisDependency.LEFT)
            ) {
                override fun renderAxisLabels(canvas: Canvas?) {
                    if (!mXAxis.isEnabled || !mXAxis.isDrawLabelsEnabled) return
                    val pointF = MPPointF.getInstance(0f, 0f)
                    mAxisLabelPaint.typeface = mXAxis.typeface
                    mAxisLabelPaint.textSize = mXAxis.textSize
                    mAxisLabelPaint.color = mXAxis.textColor
                    pointF.x = 0.5f
                    pointF.y = -0.5f
                    drawLabels(canvas, mViewPortHandler.contentBottom() - mXAxis.yOffset, pointF)
                }
            })

            with(axisLeft) {
                axisMaximum = 160F
                axisMinimum = 60F
                setDrawLabels(false)
            }
            with(axisRight) {
                axisMaximum = 160F
                axisMinimum = 60F
                textSize = ChartTextSize.AxisR.value
            }


            val entries: ArrayList<BarEntry> = ArrayList()
            for (i in 0..6) {
                entries.add(BarEntry(i.toFloat(), 0F))
            }

            val barDataSet = BarDataSet(entries, "")
            with(barDataSet) {
                valueTextSize = ChartTextSize.Cost.value
            }

            data = BarData(barDataSet)
        }
    }

    fun setValue(list: List<BPBean>, chart: BarChart, context: Context) {

        val last7Data = list.take(7)

        val entries: ArrayList<BarEntry> = ArrayList()
        for (i in 0..6) {
            entries.add(
                BarEntry(
                    i.toFloat(),
                    floatArrayOf(InitValue.Entries.value, InitValue.Entries.value)
                )
            )
        }

        var sbp: Float
        var dbp: Float
        last7Data.forEachIndexed { i, data ->

            sbp = data.bloodSBP?.toFloat() ?: 0F
            dbp = data.bloodDBP?.toFloat() ?: 0F
            if (dbp > sbp) sbp = dbp
            sbp -= dbp
            entries[6 - i] = BarEntry((6 - i).toFloat(), floatArrayOf(dbp, sbp))
        }

        chart.apply {

            val minValue = last7Data.mapNotNull { it.bloodDBP?.toInt() }.minOf { it }
            var minNum = BigDecimal(minValue / 10.0 - 2.5).toInt()
            minNum = if (minNum < 0) 0 else minNum
            minNum *= 10
            axisLeft.axisMinimum = minNum.toFloat()
            axisRight.axisMinimum = minNum.toFloat()

            val maxValue = last7Data.mapNotNull { it.bloodSBP?.toInt() }.maxOf { it }
            val maxLine = if (maxValue < LimitValue.YHigh.value)
                ((maxValue - minNum) * TopSpace.Half.value) else
                ((maxValue - minNum) * TopSpace.Min.value)
            axisLeft.axisMaximum = maxLine + minNum
            axisRight.axisMaximum = maxLine + minNum

            val barDataSet = BarDataSet(entries, "")
            with(barDataSet) {
                valueTextSize = ChartTextSize.Cost.value
                colors = mutableListOf(
                    ContextCompat.getColor(context, R.color.bar_blood_pressure_dbp),
                    ContextCompat.getColor(context, R.color.bar_blood_pressure_sbp)
                )
            }

            val barData = BarData(barDataSet)

            with(barData) {

                barWidth = BcWith.Size.value

                setValueFormatter(object : ValueFormatter() {

                })

                setValueFormatter(object : ValueFormatter() {
                    var count = -1
                    var dbpInt = 0
                    override fun getFormattedValue(value: Float): String {
                        if (value == InitValue.Entries.value) return ""

                        count++
                        return if (count % 2 == 0) {
                            dbpInt = value.toInt()
                            dbpInt.toString()
                        } else {
                            "${value.toInt() + dbpInt}"
                        }
                    }
                })
            }

            with(xAxis) {

                textSize = ChartTextSize.AxisX.value
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return try {
                            DateUtil.instance.clipTimeToHm(
                                last7Data[(6 - value.toInt())].bloodStartTime ?: ""
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