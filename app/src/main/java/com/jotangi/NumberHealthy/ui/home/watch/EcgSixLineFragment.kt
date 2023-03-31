package com.jotangi.NumberHealthy.ui.home.watch

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.Module.GlobalVariable
import com.jotangi.NumberHealthy.databinding.FragmentEcgSixLineBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding

class EcgSixLineFragment : BaseFragment() {

    private lateinit var binding: FragmentEcgSixLineBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private val xLength = 833
    private val data = GlobalVariable.getEcgData()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEcgSixLineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarArrow("ECG測量紀錄")

        binding.apply {
            tvEcgHeart.text = data.hr.toString()
            val sbp = if (data.sbp == 0) "--" else data.sbp.toString()
            val dbp = if (data.dbp == 0) "--" else data.dbp.toString()
            tvEcgBlood.text = "$sbp/$dbp"

            tvEcgHrv.text = data.hrv.toString()
        }
        initChart()
    }

    private fun initChart() {
        try {
            var value_entries = mutableListOf<Entry>()
            value_entries.add(Entry(0f, 0f))

            var value_dataset = LineDataSet(value_entries, null)

            value_dataset.apply {
                color = Color.rgb(250, 106, 131)
                valueTextColor = Color.BLACK
                setDrawCircles(false)
                setDrawValues(false)
            }

            binding.ecgChart.apply {
                isDragDecelerationEnabled = false
                setTouchEnabled(false)
                xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    setDrawLabels(false)
                    axisMinimum = 0f
                    axisMaximum = xLength.toFloat()
                    labelCount = 20
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return value.toString()
                        }
                    }
                }

                axisRight.isEnabled = false
                axisLeft.apply {
                    setDrawLabels(false)
                    axisMinimum = -930F
                    axisMaximum = 930F
                    labelCount = 20
                }
                setBackgroundColor(Color.WHITE)
                legend.isEnabled = false
                description.isEnabled = false
                data = LineData(value_dataset)
                invalidate()
            }

            drawEcg()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun drawEcg() {
        binding.ecgChart.clearValues()

        var set: LineDataSet
        val baseList = floatArrayOf(775F, 465F, 155F, -155F, -465F, -775F)
        var index = 0
        val dataValue = data.ecgValue!!.split(",")

        for (i in baseList.indices) {

            set = createSet()
            binding.ecgChart.data.addDataSet(set)

            for (j in 0..xLength) {

                set.addEntry(Entry(j.toFloat(), dataValue[index].toInt() + baseList[i]))
                index += 1

                if (index > dataValue.size - 1) {
                    break
                }
            }
        }

        binding.ecgChart.notifyDataSetChanged()
        binding.ecgChart.invalidate()
    }

    private fun createSet(): LineDataSet {
        val set = LineDataSet(null, "")
        set.apply {
            color = Color.rgb(250, 106, 131)
            setDrawCircles(false)
            setDrawValues(false)
        }
        return set
    }
}