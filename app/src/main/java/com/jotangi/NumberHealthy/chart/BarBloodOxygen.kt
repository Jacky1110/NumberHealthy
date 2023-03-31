package com.jotangi.NumberHealthy.chart

import android.content.Context
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
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.api.watch.OxygenBean
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.constant.*
import java.math.BigDecimal
import kotlin.coroutines.coroutineContext

class BarBloodOxygen private constructor() {

    companion object {
        val instance by lazy { BarBloodOxygen() }
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
                axisMaximum = 100F
                axisMinimum = 60F
                setDrawLabels(false)
            }
            with(axisRight) {
                axisMaximum = 100F
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

    fun setValue(list: List<OxygenBean>, chart: BarChart, context: Context) {

        val last7Data = list.take(7)

        val entries: ArrayList<BarEntry> = ArrayList()
        for (i in 0..6) {
            entries.add(BarEntry(i.toFloat(), InitValue.Entries.value))
        }

        val colorList = MutableList(7) {
            Color.parseColor(context.resources.getStringArray(R.array.ryg_color)[0])
        }

        last7Data.forEachIndexed { i, data ->
            colorList[6 - i] = colorSelect(data.OOValue?.toInt() ?: 0, context)
            entries[6 - i] = BarEntry((6 - i).toFloat(), data.OOValue?.toFloat() ?: 0F)
        }

        chart.apply {

            val minValue = last7Data.mapNotNull { it.OOValue?.toInt() }.minOf { it }
            var minLine = BigDecimal(minValue / 10.0 - 2.5).toInt()
            minLine = if (minLine < 0) 0 else minLine
            minLine *= 10
            axisLeft.axisMinimum = minLine.toFloat()
            axisRight.axisMinimum = minLine.toFloat()

            val barDataSet = BarDataSet(entries, "")
            with(barDataSet) {
                valueTextSize = ChartTextSize.Cost.value
                colors = colorList
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
                                last7Data[6 - value.toInt()].startTime ?: ""
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

    private fun colorSelect(num: Int, context: Context): Int {

        val colorList = context.resources.getStringArray(R.array.ryg_color)
        return when (num) {
            in 0..89 -> Color.parseColor(colorList[0])
            in 90..94 -> Color.parseColor(colorList[1])
            else -> Color.parseColor(colorList[2])
        }
    }
}