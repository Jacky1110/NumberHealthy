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
import com.jotangi.NumberHealthy.api.watch.GetStepsBean
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.MathUtil
import com.jotangi.NumberHealthy.utils.constant.*

class LineStep private constructor() {

    private val TAG = "${javaClass.simpleName}(TAG)"

    companion object {
        val instance by lazy { LineStep() }
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
                axisMaximum = RangeStep.YMax.value
                axisMinimum = RangeStep.YMin.value
                setDrawLabels(false)
            }
            with(axisRight) {
                axisMaximum = RangeStep.YMax.value
                axisMinimum = RangeStep.YMin.value
                textSize = ChartTextSize.AxisR.value

                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return value.toInt().toString()
                    }
                }
            }

            val set = LineDataSet(listOf(), "")
            data = LineData(set)
        }
    }

    fun setValue(list: List<GetStepsBean>, chart: LineChart) {

        val dataList = list.take(7)
        val timeList = DateUtil.instance.current7Ymd()
        val entries = ArrayList<Entry>()

        title@ for (i in timeList.indices) {

            for (j in dataList.indices) {
                if (dataList[j].sportStartTime?.contains(timeList[i]) == true) {
                    entries.add(
                        Entry(i.toFloat(), dataList[j].sportStep?.toFloat() ?: 0F)
                    )
                    continue@title
                }
            }

            entries.add(Entry(i.toFloat(), 0F))
        }

        val maxValue = entries.map { it.y }.maxOf { it }
        if (maxValue == 0F) return

        chart.apply {

            val maxLine = maxValue * TopSpace.Min.value
            axisLeft.axisMaximum = maxLine
            axisRight.axisMaximum = maxLine

            val set = LineDataSet(entries, "")
            with(set) {

                valueTextSize = ChartTextSize.Cost.value

                color = Color.parseColor(StepColor.Theme.value)
                lineWidth = LineCommon.LineWith.value

                setDrawCircleHole(false)
                setCircleColor(Color.parseColor(StepColor.Theme.value))
            }

            data = LineData(set)
            invalidate()
        }
    }
}