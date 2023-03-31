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
import com.jotangi.NumberHealthy.api.watch.HeartRateBean
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.constant.*
import java.math.BigDecimal

class BarHeartRate private constructor() {

    companion object {
        val instance by lazy { BarHeartRate() }
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
                axisMaximum = LimitValue.YHigh.value
                axisMinimum = LimitValue.YLow.value
                setDrawLabels(false)
            }
            with(axisRight) {
                axisMaximum = LimitValue.YHigh.value
                axisMinimum = LimitValue.YLow.value
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

    fun setValue(list: List<HeartRateBean>, chart: BarChart, context: Context) {

        val last7Data = list.take(7)

        val entries: ArrayList<BarEntry> = ArrayList()
        for (i in 0..6) {
            entries.add(BarEntry(i.toFloat(), InitValue.Entries.value))
        }

        val colorList = MutableList(7) {
            Color.parseColor(context.resources.getStringArray(R.array.heart_rate_color)[0])
        }

        last7Data.forEachIndexed { i, data ->
            colorList[6 - i] = colorSelect(data.heartValue?.toInt() ?: 0, context)
            entries[6 - i] = BarEntry((6 - i).toFloat(), data.heartValue?.toFloat() ?: 0F)
        }

        chart.apply {

            val minValue = last7Data.mapNotNull { it.heartValue?.toInt() }.minOf { it }
            var minNum = BigDecimal(minValue / 10.0 - 0.5).toInt()
            minNum = if (minNum < 0) 0 else minNum
            minNum *= 10
            val minLine = if (minValue < 66) minNum else 60
            axisLeft.axisMinimum = minLine.toFloat()
            axisRight.axisMinimum = minLine.toFloat()

            val maxValue = last7Data.mapNotNull { it.heartValue?.toInt() }.maxOf { it }
            val maxLine = if (maxValue < LimitValue.YHigh.value)
                ((maxValue - minLine) * TopSpace.Half.value) else
                ((maxValue - minLine) * TopSpace.Min.value)
            axisLeft.axisMaximum = maxLine + minLine
            axisRight.axisMaximum = maxLine + minLine

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
                                last7Data[6 - value.toInt()].heartStartTime ?: ""
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

        val colorList = context.resources.getStringArray(R.array.heart_rate_color)
        return when (num) {
            in 0..117 -> Color.parseColor(colorList[0])
            in 118..137 -> Color.parseColor(colorList[1])
            in 138..156 -> Color.parseColor(colorList[2])
            in 157..176 -> Color.parseColor(colorList[3])
            else -> Color.parseColor(colorList[4])
        }
    }
}