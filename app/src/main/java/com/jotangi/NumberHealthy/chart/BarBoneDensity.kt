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
import com.jotangi.NumberHealthy.api.watch.GetBmdBean
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.MathUtil
import com.jotangi.NumberHealthy.utils.constant.*

class BarBoneDensity private constructor() {

    private val TAG = "${javaClass.simpleName}(TAG)"

    companion object {
        val instance by lazy { BarBoneDensity() }
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
                axisMaximum = RangeBone.Max.value
                axisMinimum = RangeBone.Mini.value
                setDrawLabels(false)
                setLabelCount(5, true)
            }
            with(axisRight) {
                axisMaximum = RangeBone.Max.value
                axisMinimum = RangeBone.Mini.value
                textSize = ChartTextSize.AxisR.value

                setLabelCount(5, true)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return when (value.toInt()) {
                            6 -> "2"
                            4 -> "0.5"
                            3 -> "-1"
                            1 -> "-2.5"
                            0 -> "-4"
                            else -> value.toInt().toString()
                        }
                    }
                }
            }


            val entries: ArrayList<BarEntry> = ArrayList()
            for (i in 0..6) {
                entries.add(BarEntry(i.toFloat(), 0F))
            }

            val barDataSet = BarDataSet(entries, "")
            with(barDataSet) {
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return ""
                    }
                }
            }

            data = BarData(barDataSet)
            invalidate()
        }
    }

    fun setValue(list: List<GetBmdBean>, chart: BarChart) {

        val last7Data = list.take(7)

        val entries: ArrayList<BarEntry> = ArrayList()
        for (i in 0..6) {
            entries.add(BarEntry(i.toFloat(), InitValue.Entries.value))
        }

        val colorList = MutableList(7) {
            Color.parseColor(BoneColor.Red.value)
        }

        var value: Float
        last7Data.forEachIndexed { i, data ->
            value = (data.TScore?.toFloat() ?: InitValue.Entries.value)
            colorList[6 - i] = colorSelect(value)
            entries[6 - i] = BarEntry((6 - i).toFloat(), value + 4)
        }

        chart.apply {

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
                            "${value.barValue()}" else ""
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

    private fun Float.barValue(): String {
        return MathUtil.instance.fixBoneValue((this - 4).toString())
    }

    private fun colorSelect(num: Float): Int {

        return when (num) {
            in -1F..2F -> Color.parseColor(BoneColor.Green.value)
            in -4F..-2.5F -> Color.parseColor(BoneColor.Red.value)
            in -2.5F..-1F -> Color.parseColor(BoneColor.Yellow.value)
            else -> Color.parseColor(BoneColor.Red.value)
        }
    }
}