package com.jotangi.NumberHealthy.ui.home.watch

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.api.watch.ElementRequest
import com.jotangi.NumberHealthy.api.watch.UploadBPRequest
import com.jotangi.NumberHealthy.api.watch.WatchApiRepository
import com.jotangi.NumberHealthy.chart.BarBloodPressure
import com.jotangi.NumberHealthy.databinding.FragmentBloodPressureBinding
import com.jotangi.NumberHealthy.ui.home.BoneDensityFragmentArgs
import com.jotangi.NumberHealthy.ui.home.bar.HorizontalColorBar
import com.jotangi.NumberHealthy.utils.AppUtil
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.DialogDateUtil
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.yucheng.ycbtsdk.YCBTClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class BloodPressureFragment : BaseFragment() {

    private lateinit var binding: FragmentBloodPressureBinding
    override fun getToolBar() = binding.toolbar

    private val args by navArgs<BoneDensityFragmentArgs>()
    private val apiRepository: WatchApiRepository by lazy { WatchApiRepository() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBloodPressureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initHandler()
        initCallBack()
    }

    private fun initView() {

        setToolbarDetail(
            getString(R.string.circle_blood_pressure),
            BloodPressureFragmentDirections
                .actionBloodPressureFragmentToBloodPressureDetailFragment(
                    args.tel
                )
        )

        binding.apply {

            BarBloodPressure.instance.init(bc)

            if (AppUtil.instance.checkSelf(
                    SharedPreferencesUtil.instances.getAccountId(),
                    args.tel
                )
            ) {
                btFixBloodPressure.visibility = View.GONE
                btInsertData.visibility = View.GONE
            }
        }
    }

    private fun initHandler() {

        binding.btFixBloodPressure.setOnClickListener {

            if (SharedPreferencesUtil.instances.getWatchMac() != null) {
                pickerDialog()
            } else {
                Toast.makeText(context, "請先綁定手錶", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btInsertData.setOnClickListener {

            DialogDateUtil.instance.bloodPressure(
                requireActivity(),
                request = { refreshData(it) }
            )
        }
    }

    private fun pickerDialog() {
        val dialog = AlertDialog.Builder(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_bp_picker, null)
        dialog.apply {
            setCancelable(false)
            setTitle("")
            setView(view)
            val picker1 = view.findViewById(R.id.picker1) as NumberPicker
            picker1.apply {
                minValue = 60
                maxValue = 250
                wrapSelectorWheel = false
            }
            val picker2 = view.findViewById(R.id.picker2) as NumberPicker
            picker2.apply {
                minValue = 40
                maxValue = 150
                wrapSelectorWheel = false
            }
            setPositiveButton("確認") { dialogInterface, i ->

                YCBTClient.appBloodCalibration(
                    picker1.value,
                    picker2.value
                ) { i, fl, hashMap ->

                    if (i == 0 && hashMap != null) {
                        GlobalScope.launch(Dispatchers.Main) {
                            Log.d(TAG, "$i , $fl , ${hashMap.get("data").toString()}")
                            Toast.makeText(requireContext(), "校正完成", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            setNegativeButton("取消") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            show()
        }
    }

    private fun refreshData(request: UploadBPRequest) {

        lifecycleScope.launch {

            val resp = apiRepository.uploadBp(request)
            if (resp.code != "0x0200") return@launch

            watchViewModel.getBp(
                ElementRequest(
                    SharedPreferencesUtil.instances.getAccountId() ?: "",
                    DateUtil.instance.ago3MonthYmdHms(),
                    DateUtil.instance.currentYmdHms()
                )
            )
        }
    }

    private fun initCallBack() {

        watchViewModel.lastBpBean.observe(viewLifecycleOwner) {

            if (it.isNullOrEmpty()) return@observe

            binding.apply {

                tvDate.text = DateUtil.instance.clipTimeFormatSecond(
                    it[0].bloodStartTime
                )

                tvValue.text = "${it[0].bloodSBP} / ${it[0].bloodDBP}"
                heartRateColorBarSBP.setDataValue(it[0].bloodSBP!!.toInt())
                heartRateColorBarDBP.setDataValue(it[0].bloodDBP!!.toInt())

                BarBloodPressure.instance.setValue(it, bc, requireContext())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        watchViewModel.lastBpBean.postValue(listOf())
    }
}