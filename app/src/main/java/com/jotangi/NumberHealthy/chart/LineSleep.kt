package com.jotangi.NumberHealthy.chart

import android.graphics.Canvas
import android.graphics.Color
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF
import com.jotangi.NumberHealthy.api.watch.SleepLineData
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.MathUtil
import com.jotangi.NumberHealthy.utils.constant.*

class LineSleep private constructor() {

    private val TAG = "${javaClass.simpleName}(TAG)"

    companion object {
        val instance by lazy { LineSleep() }
    }


    fun init(chart: LineChart) {

        chart.apply {

            setTouchEnabled(false)
            legend.isEnabled = false
            description.isEnabled = false

            with(xAxis) {

                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTH_SIDED
                textSize = ChartTextSize.AxisX.value
                axisMaximum = LineCommon.XHigh.value
                axisMinimum = LineCommon.XLow.value

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
                axisMaximum = LineCommon.HourHigh.value
                axisMinimum = LineCommon.HourLow.value
                setDrawLabels(false)
            }
            with(axisRight) {
                axisMaximum = LineCommon.HourHigh.value
                axisMinimum = LineCommon.HourLow.value
                textSize = ChartTextSize.AxisR.value
            }

            val sets = ArrayList<ILineDataSet>()

            val entryDeep: ArrayList<Entry> = ArrayList()
            val entryTotal: ArrayList<Entry> = ArrayList()
            for (i in 0..6) {
                entryDeep.add(Entry(i.toFloat(), 0F))
                entryTotal.add(Entry(i.toFloat(), 0F))
            }

            val setDeep = LineDataSet(entryDeep, "")
            val setTotal = LineDataSet(entryTotal, "")
            with(setDeep) {

                valueTextSize = ChartTextSize.Cost.value
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "0"
                    }
                }

                color = Color.parseColor(SleepColor.Deep.value)
                lineWidth = LineCommon.LineWith.value

                setDrawCircleHole(false)
                setCircleColor(Color.parseColor(SleepColor.Deep.value))
            }

            with(setTotal) {

                valueTextSize = ChartTextSize.Cost.value
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "0"
                    }
                }

                color = Color.parseColor(SleepColor.Total.value)
                lineWidth = LineCommon.LineWith.value

                setDrawCircleHole(false)
                setCircleColor(Color.parseColor(SleepColor.Total.value))
            }

            sets.add(setDeep)
            sets.add(setTotal)
            data = LineData(sets)
        }
    }

    fun setValue(list: SleepLineData, chart: LineChart) {

        var initMaxValue = LineInit.InitMax.value
        var initMinValue = LineInit.InitMin.value

        list.deepMinuteList.forEach {
            if (it > initMaxValue) initMaxValue = it
            if (it < initMinValue) initMinValue = it
        }
        list.totalMinuteList.forEach {
            if (it > initMaxValue) initMaxValue = it
            if (it < initMinValue) initMinValue = it
        }
        if (initMaxValue == LineInit.InitMax.value ||
            initMinValue == LineInit.InitMin.value) return

        initMaxValue = MathUtil.instance.fixUpToInt(initMaxValue / 60F)
        initMinValue = MathUtil.instance.fixDownToInt(initMinValue / 60F)
        if (initMaxValue == LineInit.Error.value ||
            initMinValue == LineInit.Error.value) return

        chart.apply {

            with(axisLeft) {
                axisMaximum = initMaxValue.toFloat()
                axisMinimum = initMinValue.toFloat()
            }

            with(axisRight) {
                axisMaximum = initMaxValue.toFloat()
                axisMinimum = initMinValue.toFloat()
            }

            val sets = ArrayList<ILineDataSet>()

            val entryDeep: ArrayList<Entry> = ArrayList()
            val entryTotal: ArrayList<Entry> = ArrayList()
            for (i in 0..6) {
                entryDeep.add(Entry(i.toFloat(), list.deepMinuteList[i] / 60F))
                entryTotal.add(Entry(i.toFloat(), list.totalMinuteList[i] / 60F))
            }

            val setDeep = LineDataSet(entryDeep, "")
            val setTotal = LineDataSet(entryTotal, "")
            with(setDeep) {

                valueTextSize = ChartTextSize.Cost.value
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return MathUtil.instance.fixSleepValue(value)
                    }
                }

                color = Color.parseColor(SleepColor.Deep.value)
                lineWidth = LineCommon.LineWith.value

                setDrawCircleHole(false)
                setCircleColor(Color.parseColor(SleepColor.Deep.value))
            }

            with(setTotal) {

                valueTextSize = ChartTextSize.Cost.value
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return MathUtil.instance.fixSleepValue(value)
                    }
                }

                color = Color.parseColor(SleepColor.Total.value)
                lineWidth = LineCommon.LineWith.value

                setDrawCircleHole(false)
                setCircleColor(Color.parseColor(SleepColor.Total.value))
            }

            sets.add(setDeep)
            sets.add(setTotal)
            data = LineData(sets)
            invalidate()
        }
    }
}