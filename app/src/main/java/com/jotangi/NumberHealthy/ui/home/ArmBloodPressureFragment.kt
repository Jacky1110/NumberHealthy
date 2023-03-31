package com.jotangi.NumberHealthy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.api.watch.UploadArmRequest
import com.jotangi.NumberHealthy.api.watch.ElementRequest
import com.jotangi.NumberHealthy.databinding.FragmentArmBloodPressureBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.ui.home.bar.HorizontalColorBar
import com.jotangi.NumberHealthy.utils.AppUtil
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.DialogDateUtil
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.jotangi.NumberHealthy.api.watch.WatchApiRepository
import kotlinx.coroutines.launch

class ArmBloodPressureFragment : BaseFragment() {

    private lateinit var binding: FragmentArmBloodPressureBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private val args by navArgs<ArmBloodPressureFragmentArgs>()
    private val apiRepository: WatchApiRepository by lazy { WatchApiRepository() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArmBloodPressureBinding.inflate(inflater, container, false)
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
            getString(R.string.circle_arm_blood_pressure),
            R.id.armBloodPressureDetailFragment
        )

        if (AppUtil.instance.checkSelf(
                SharedPreferencesUtil.instances.getAccountId(),
                args.tel
            )
        ) binding.btInsertData.visibility = View.GONE
    }

    private fun initHandler() {

        binding.btInsertData.setOnClickListener {

            DialogDateUtil.instance.arm(
                requireActivity(),
                watchViewModel.getBp2List.value,
                request = { refreshData(it) }
            )
        }
    }

    private fun refreshData(request: UploadArmRequest) {

        lifecycleScope.launch {

            val response = apiRepository.uploadBp2(request)
            if (response.code != "0x0200") return@launch

            watchViewModel.getBp2(
                ElementRequest(
                    SharedPreferencesUtil.instances.getAccountId() ?: "",
                    DateUtil.instance.ago3MonthYmdHms(),
                    DateUtil.instance.currentYmdHms()
                )
            )
        }
    }

    private fun initCallBack() {

        watchViewModel.getBp2List.observe(viewLifecycleOwner) { list ->

            if (list.isNotEmpty()) {
                binding.apply {

                    with(list[0]) {
                        tvDate.text = DateUtil.instance.clipTimeFormatSecond(bloodStartTime!!)
                        tvValue.text = heartValue

                        hcbSbp.setType(HorizontalColorBar.Type.DBP)
                        hcbDbp.setType(HorizontalColorBar.Type.SBP)

                        // 90 - 140
                        hcbSbp.setDataValue(LbloodSBP!!.toInt())
                        // 60 -90
                        hcbDbp.setDataValue(LbloodDBP!!.toInt())

                        hcbSbpR.setType(HorizontalColorBar.Type.DBP)
                        hcbDbpR.setType(HorizontalColorBar.Type.SBP)

                        // 90 - 140
                        hcbSbpR.setDataValue(RbloodSBP!!.toInt())
                        // 60 -90
                        hcbDbpR.setDataValue(RbloodDBP!!.toInt())


                        etLSys.text = LbloodSBP
                        etRSys.text = RbloodSBP
                        etLDia.text = LbloodDBP
                        etRDia.text = RbloodDBP
                        etLPp.text = LbloodPP
                        etRPp.text = RbloodPP
                        etLMap.text = LbloodMAP
                        etRMap.text = RbloodMAP
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        watchViewModel.getBp2List.postValue(listOf())
    }
}