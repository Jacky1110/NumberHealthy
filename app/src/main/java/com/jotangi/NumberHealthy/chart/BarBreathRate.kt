package com.jotangi.NumberHealthy.chart

import android.graphics.Canvas
import android.graphics.Color
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.jotangi.NumberHealthy.api.watch.BreathRateBean
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.constant.*

class BarBreathRate private constructor() {

    private val TAG = "${javaClass.simpleName}(TAG)"

    companion object {
        val instance by lazy { BarBreathRate() }
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
                axisMaximum = 30F
                axisMinimum = 0F
                setDrawLabels(false)
            }

            with(axisRight) {

                axisMaximum = 30F
                axisMinimum = 0F
                textSize = ChartTextSize.AxisR.value

                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "${value.toInt()}次"
                    }
                }
            }

            val entries: ArrayList<BarEntry> = ArrayList()
            for (i in 0..6) {
                entries.add(BarEntry(i.toFloat(), 0F))
            }

            val barData = BarData(BarDataSet(entries, ""))
            with(barData) {
                setValueFormatter(object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return ""
                    }
                })
            }

            data = barData
            invalidate()
        }
    }

    fun setValue(list: List<BreathRateBean>, chart: BarChart) {

        val last7Data = list.take(7)
        val entries: ArrayList<BarEntry> = ArrayList()
        for (i in 0..6) {
            entries.add(BarEntry(i.toFloat(), InitValue.Entries.value))
        }

        for (i in last7Data.indices) {
            entries[6 - i] = BarEntry((6 - i).toFloat(), last7Data[i].respiratoryrate!!.toFloat())
        }

        chart.apply {

            val maxValue = last7Data.mapNotNull { it.respiratoryrate?.toInt() }.maxOf { it }
            val maxLine = if (maxValue > 30)
                (maxValue * TopSpace.Min.value).toInt() else
                (maxValue * TopSpace.High.value).toInt()
            axisLeft.axisMaximum = maxLine.toFloat()
            axisRight.axisMaximum = maxLine.toFloat()

            val barDataSet = BarDataSet(entries, "")
            with(barDataSet) {
                valueTextSize = ChartTextSize.Cost.value
                color = Color.parseColor(BreathRateColor.Theme.value)
            }

            val barData = BarData(barDataSet)

            with(barData) {

                barWidth = BcWith.Size.value

                setValueFormatter(object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return if (value != InitValue.Entries.value)
                            "${value.toInt()}" else ""
                    }
                })
            }

            with(xAxis) {

                textSize = ChartTextSize.AxisX.value
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