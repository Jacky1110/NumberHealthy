package com.jotangi.NumberHealthy.ui.home.watch

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.api.watch.UploadEcgRequest
import com.jotangi.NumberHealthy.api.watch.ElementRequest
import com.jotangi.NumberHealthy.databinding.FragmentEcgBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.ui.ecg.EcgDataCallback
import com.jotangi.NumberHealthy.utils.AppUtil
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.DialogUtils
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.jotangi.NumberHealthy.api.watch.WatchApiRepository
import com.jotangi.NumberHealthy.utils.smartwatch.WatchUtils
import com.yucheng.ycbtsdk.Constants
import com.yucheng.ycbtsdk.YCBTClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class EcgFragment : BaseFragment() {

    private lateinit var binding: FragmentEcgBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private var btn_status = true
    private val memberId = SharedPreferencesUtil.instances.getAccountId()

    private var count = 0
    protected val apiRepository: WatchApiRepository by lazy { WatchApiRepository() }
    private val args by navArgs<EcgFragmentArgs>()

    private val xLength = 833
    private var dataValue = ArrayList<Float>()
    private lateinit var sendValue: ArrayList<Int>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEcgBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initAction()
    }

    private fun initView() {

        watchViewModel.getWatchInfo()

        initChart()

        setToolbarDetail(
            getString(R.string.circle_ecg),
            R.id.ecgDetailFragment
        )

        if (AppUtil.instance.checkSelf(memberId, args.tel)) {
            binding.apply {
                linearLayout18.visibility = View.GONE
                bnECGStart.visibility = View.GONE
            }
        }
    }

    private fun initAction() {

        binding.bnECGStart.apply {

            setOnClickListener {

                if (SharedPreferencesUtil.instances.getWatchMac() != null &&
                    YCBTClient.connectState() == Constants.BLEState.ReadWriteOK
                ) {

                    if (btn_status) {

                        DialogUtils.showMyDialog(
                            rootView as ViewGroup?,
                            R.string.ecg_start_title,
                            R.string.ecg_start,
                            "",
                            R.string.ecg_start_ok
                        ) {
                            binding.tvEcgHrv.text = "- -"
                            binding.tvEcgHeart.text = "- -"
                            binding.tvEcgBlood.text = "- -/- -"
                            binding.tvProgress.text = "0%"
                            binding.ecgChart.visibility = View.VISIBLE
                            binding.ecgSixLineChart.visibility = View.GONE
                            dataValue = ArrayList()
                            binding.ecgGoodTag.visibility = View.INVISIBLE
                            initChart()
                            binding.ecgProgressBar.progress = 0
                            count = 0
                            setImageDrawable(resources.getDrawable(R.drawable.ic_ecg_stop))
                            btn_status = false

                            watchViewModel.appECGTest(object : EcgDataCallback.Start {
                                override fun receiveECG(value: Float) {
//                                    Log.i(TAG, "receiveECG: $value")
                                    dataValue.add(value)
                                    lifecycleScope.launch(Dispatchers.Main) {
                                        updateGraph(value)
                                    }
                                }

                                override fun receiveHRV(value: Int) {
                                    Log.d(TAG, "receiveHRV: $value")
                                    binding.tvEcgHrv.text = value.toString()
                                }

                                override fun receiveBlood(
                                    heartValue: Int,
                                    bloodDBP: Int,
                                    bloodSBP: Int
                                ) {
                                    Log.d(TAG, "receiveBlood: $heartValue $bloodDBP $bloodSBP")
                                    binding.tvEcgHeart.text = heartValue.toString()
                                    binding.tvEcgBlood.text = "$bloodDBP/$bloodSBP"
                                }

                                override fun receiveBadSignal() {
                                    Log.d(TAG, "receiveBadSignal: ")
                                    binding.ecgGoodTag.visibility = View.INVISIBLE
                                }
                            })
                        }

                    } else {
                        setImageDrawable(resources.getDrawable(R.drawable.ic_ecg_play))
                        btn_status = true
                        try {
                            watchViewModel.appECGTestEnd(object : EcgDataCallback.End {
                                override fun receive(value: Float) {
                                    Log.d(TAG,"ECG 結束: $value")
                                }

                            })
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } else {
                    Toast.makeText(context, "請先綁定手錶", Toast.LENGTH_SHORT).show()
                }
            }
        }
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
                    labelCount = 500
                    // granularity = 1f
                    valueFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return value.toString()
                        }
                    }
                }

                axisRight.isEnabled = false
                axisLeft.apply {
                    setDrawLabels(false)
                    //granularity = 1f
                    axisMinimum = -55000f
                    axisMaximum = 55000f
                    labelCount = 500
                }
                setBackgroundColor(Color.WHITE)
                legend.isEnabled = false
                description.isEnabled = false
                data = LineData(value_dataset)
                //if(sendData==null)
                //sendData = data
                invalidate()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 更新畫圖
    private fun updateGraph(value: Float) {
        try {
            binding.ecgGoodTag.visibility = View.VISIBLE
            val data: LineData = binding.ecgChart.data
            val set: LineDataSet? = data.getDataSetByIndex(0) as LineDataSet?
            //val set2: LineDataSet? = data.getDataSetByIndex(0) as LineDataSet?
            if (set != null) {
                data.addEntry(
                    Entry(
                        set.entryCount.toFloat(),
                        value
                    ), 0
                )
                /*sendData?.addEntry(
                    Entry(
                        set2!!.entryCount.toFloat(),
                        value
                    ), 0
                )*/
                if (set.entryCount >= xLength) {
                    set.removeFirst()
                    for (i in 0 until set.entryCount) {
                        val entryToChange = set.getEntryForIndex(i)
                        entryToChange.x = entryToChange.x - 1
                    }
                }
                binding.ecgChart.notifyDataSetChanged()
                binding.ecgChart.invalidate()
                if (count > 70) {
                    updateProgress()
                    count = 0
                } else
                    count++
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateProgress() {

        binding.apply {

            var progressNum = dataValue.size / 150

            if (progressNum < 100) {

                ecgProgressBar.progress = progressNum

                if (progressNum > 100) {
                    progressNum = 100
                }
                binding.tvProgress.text = "$progressNum%"
            } else {

                Log.w(TAG, "dataValue.size: ${dataValue.size}")

                tvProgress.text = "0%"
                bnECGStart.setImageDrawable(resources.getDrawable(R.drawable.ic_ecg_play))
                btn_status = true
                ecgProgressBar.progress = 0
                count = 0

                initSixLineChart()

                uploadECG(
                    tvEcgHeart.text.toString(),
                    tvEcgBlood.text.toString().split("/")[0],
                    tvEcgBlood.text.toString().split("/")[1],
                    tvEcgHrv.text.toString()
                )

                try {
                    watchViewModel.appECGTestEnd(object : EcgDataCallback.End {
                        override fun receive(value: Float) {
                            Log.d(TAG,"ECG 結束: $value")
                            ecgGoodTag.visibility = View.INVISIBLE
                        }
                    })
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                //findNavController().navigate(EcgFragmentDirections.actionNavEcgToNavEcgDetail(LineDataModel(sendData!!)))
                //initChart()
                //sendData = null
            }
        }
    }

    private fun initSixLineChart() {

        binding.ecgSixLineChart.apply {
            clear()

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


                drawEcg()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun drawEcg() {

        binding.ecgSixLineChart.apply {

            clearValues()

            var set: LineDataSet
            val baseList = floatArrayOf(775F, 465F, 155F, -155F, -465F, -775F)
            var index = 0
            var value: Int
            sendValue = ArrayList()

            for (i in baseList.indices) {

                set = createSet()
                data.addDataSet(set)

                for (j in 0..xLength) {

                    value = processValue(dataValue[index])
                    sendValue.add(value)

//                set.addEntry(Entry(j.toFloat(), (Math.random() * 100).toInt() + baseList[i]) )
                    set.addEntry(Entry(j.toFloat(), value + baseList[i]))
                    index += 3

                    if (index > dataValue.size - 1) {
                        break
                    }
                }
            }
            Log.w(TAG, "sendValue: $sendValue")

            notifyDataSetChanged()
            invalidate()

            binding.ecgChart.visibility = View.GONE
            visibility = View.VISIBLE
        }
    }

    private fun processValue(value: Float): Int {
        var num = (value * 5 / 1000).toInt()
        num = if (num > 155) 155 else num
        num = if (num < -155) -155 else num
        return num
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

    private fun uploadECG(hr: String, sbp: String, dbp: String, hrv: String) {

        if (hrv != "- -" && hrv != "0" && hr != "- -" && hr != "0" && dbp != "- -" && dbp != "0" && sbp != "- -" && sbp != "0") {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val date = sdf.format(Date())

            val stringBuffer = StringBuffer()
            for (i in sendValue.indices) {
                stringBuffer.append("${sendValue[i]},")
            }
            stringBuffer.setLength(stringBuffer.length - 1)

            CoroutineScope(Dispatchers.IO).launch {
                val resp = apiRepository.uploadEcg(
                    UploadEcgRequest(
                        memberId!!,
                        date,
                        stringBuffer.toString(),
                        hr.toInt(),
                        dbp.toInt(),
                        sbp.toInt(),
                        hrv.toInt()
                    )
                )

                if (resp.code == "0x0200") {

                    CoroutineScope(Dispatchers.IO).launch {

                        watchViewModel.getECG(
                            ElementRequest(
                                memberId!!,
                                DateUtil.instance.ago3MonthYmdHms(),
                                DateUtil.instance.currentYmdHms()
                            )
                        )
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            watchViewModel.appECGTestEnd(object : EcgDataCallback.End {
                override fun receive(value: Float) {
                    Log.d(TAG,"ECG 結束: $value")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
        WatchUtils.instance.initOneTimeWorker(3, TimeUnit.SECONDS)
    }

    override fun onResume() {
        super.onResume()
        WatchUtils.instance.cancelAllWorker()
    }

    override fun onDestroy() {
        super.onDestroy()
        watchViewModel.last7EcgBean.postValue(listOf())
    }
}