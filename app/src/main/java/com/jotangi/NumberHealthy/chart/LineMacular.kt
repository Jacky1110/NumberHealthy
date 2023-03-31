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
import com.jotangi.NumberHealthy.api.watch.GetMpodBean
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.MathUtil
import com.jotangi.NumberHealthy.utils.constant.*

class LineMacular private constructor() {

    private val TAG = "${javaClass.simpleName}(TAG)"

    companion object {
        val instance by lazy { LineMacular() }
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
                axisMaximum = MacularElement.YMax.value
                axisMinimum = MacularElement.YMin.value
                setLabelCount(5, true)
                setDrawLabels(false)
            }
            with(axisRight) {
                axisMaximum = MacularElement.YMax.value
                axisMinimum = MacularElement.YMin.value
                textSize = ChartTextSize.AxisR.value

                setLabelCount(5, true)
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return value.toString()
                    }
                }
            }

            val set = LineDataSet(listOf(), "")
            data = LineData(set)
        }
    }

    fun setValue(list: List<GetMpodBean>, chart: LineChart) {

        val dataList = processList(list.take(7))

        val entryLeft = ArrayList<Entry>()
        val entryRight = ArrayList<Entry>()

        var fValue: Float
        for (i in 0..6) {

            fValue = dataList.LeftList[i]
            if (fValue != MacularElement.Error.value) {
                entryLeft.add(Entry(i.toFloat(), fValue))
            }

            fValue = dataList.RightList[i]
            if (fValue != MacularElement.Error.value) {
                entryRight.add(Entry(i.toFloat(), fValue))
            }
        }

        chart.apply {

            val sets = ArrayList<ILineDataSet>()
            val setLeft = LineDataSet(entryLeft, "")
            val setRight = LineDataSet(entryRight, "")

            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return try {
                        dataList.DateList[value.toInt()]
                    } catch (e: Exception) {
                        e.printStackTrace()
                        ""
                    }
                }
            }

            with(setLeft) {

                valueTextSize = ChartTextSize.Cost.value
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return MathUtil.instance.fixMacularValue(value)
                    }
                }

                color = Color.parseColor(MacularColor.Left.value)
                lineWidth = LineCommon.LineWith.value

                setDrawCircleHole(false)
                setCircleColor(Color.parseColor(MacularColor.Left.value))
            }

            with(setRight) {

                valueTextSize = ChartTextSize.Cost.value
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return MathUtil.instance.fixMacularValue(value)
                    }
                }

                color = Color.parseColor(MacularColor.Right.value)
                lineWidth = LineCommon.LineWith.value

                setDrawCircleHole(false)
                setCircleColor(Color.parseColor(MacularColor.Right.value))
            }

            sets.add(setRight)
            sets.add(setLeft)
            data = LineData(sets)
            invalidate()
        }
    }

    private fun processList(list: List<GetMpodBean>): MacularLineData {

        val data = MacularLineData()

        for (i in list.indices) {

            data.LeftList[i] = list[i].lefteye?.toFloat() ?: MacularElement.Error.value
            data.RightList[i] = list[i].righteye?.toFloat() ?: MacularElement.Error.value
            data.DateList[i] = DateUtil.instance.clipTimeToHm(list[i].mpodStartTime ?: "")
        }

        data.LeftList = data.LeftList.reversed().toMutableList()
        data.RightList = data.RightList.reversed().toMutableList()
        data.DateList = data.DateList.reversed().toMutableList()

        return data
    }
}