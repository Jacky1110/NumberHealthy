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
import com.jotangi.NumberHealthy.api.watch.GetKcalDataBean
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.constant.BcWith
import com.jotangi.NumberHealthy.utils.constant.ChartTextSize
import com.jotangi.NumberHealthy.utils.constant.KcalColor
import com.jotangi.NumberHealthy.utils.constant.TopSpace
import java.text.DecimalFormat

class BarKcal private constructor() {

    private val TAG = "${javaClass.simpleName}(TAG)"

    companion object {
        val instance by lazy { BarKcal() }
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

                val currentlyMd = DateUtil.instance.current7Md()
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return currentlyMd[value.toInt()]
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
                axisMaximum = 100f
                axisMinimum = 0f
                setDrawLabels(false)
            }
            with(axisRight) {
                axisMaximum = 100f
                axisMinimum = 0f
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

    fun setValue(list: List<GetKcalDataBean>, chart: BarChart) {

        val dateList = list.take(7)
        val timeList = DateUtil.instance.current7Ymd()
        val entries: ArrayList<BarEntry> = ArrayList()

        title@ for (i in timeList.indices) {

            for (j in dateList.indices) {
                if (dateList[j].startTime?.contains(timeList[i]) == true) {
                    entries.add(
                        BarEntry(i.toFloat(), dateList[j].KCAL?.toFloat() ?: 0F)
                    )
                    continue@title
                }
            }

            entries.add(BarEntry(i.toFloat(), 0F))
        }

        val maxValue = entries.map { it.y }.maxOf { it }
        if (maxValue == 0F) return

        chart.apply {

            val maxLine = maxValue * TopSpace.Min.value
            axisLeft.axisMaximum = maxLine
            axisRight.axisMaximum = maxLine

            val barDataSet = BarDataSet(entries, "")
            with(barDataSet) {
                valueTextSize = ChartTextSize.Cost.value
                color = Color.parseColor(KcalColor.Theme.value)
            }

            val barData = BarData(barDataSet)

            with(barData) {
                barWidth = BcWith.Size.value

                setValueFormatter(object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return DecimalFormat("#,###").format(value.toInt())
                    }
                })
            }

            data = barData
            invalidate()
        }
    }
}