package com.jotangi.NumberHealthy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.api.watch.ElementRequest
import com.jotangi.NumberHealthy.databinding.FragmentCBinding
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.DialogUtils
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.jotangi.NumberHealthy.utils.constant.JiugonggeEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class CFragment : BaseFragment() {

    private lateinit var binding: FragmentCBinding
    override fun getToolBar() = binding.toolbar

    private var statusList = arrayListOf<String>()
    private var memberIdList = arrayListOf<String>()

    private lateinit var legendTV: ArrayList<TextView>
    private lateinit var backgroundV: ArrayList<View>
    private lateinit var iconIV: ArrayList<ImageView>
    private lateinit var clickCL: ArrayList<ConstraintLayout>

    private var legendStr = ""
    private var backgroundNum = 0
    private var iconNum = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initView()
    }

    private fun initData() {

        binding.apply {

            legendTV = arrayListOf(
                tvCfC1, tvCfC2, tvCfC3, tvCfC4, tvCfC5, tvCfC6, tvCfC7, tvCfC8, tvCfC9
            )

            backgroundV = arrayListOf(
                vCfC1, vCfC2, vCfC3, vCfC4, vCfC5, vCfC6, vCfC7, vCfC8, vCfC9
            )

            iconIV = arrayListOf(
                ivCfC1, ivCfC2, ivCfC3, ivCfC4, ivCfC5, ivCfC6, ivCfC7, ivCfC8, ivCfC9
            )

            clickCL = arrayListOf(
                clCfC1, clCfC2, clCfC3, clCfC4, clCfC5, clCfC6, clCfC7, clCfC8, clCfC9
            )
        }

        arguments?.getString(JiugonggeEnum.LEGEND.name, "")?.let {
            if (it.isNotBlank()) {
                legendStr = it
            }
        }

        arguments?.getInt(JiugonggeEnum.BACKGROUND.name, 0)?.let {
            if (it != 0) {
                backgroundNum = it
            }
        }

        arguments?.getInt(JiugonggeEnum.ICON.name, 0)?.let {
            if (it != 0) {
                iconNum = it
            }
        }
    }

    private fun initView() {

        setToolbarArrow("數體健")

        legendTV[0].text = "自己"

        if (backgroundNum != 0) {
            backgroundV[0].background = ResourcesCompat.getDrawable(
                resources, backgroundNum, null
            )
        }

        if (iconNum != 0) {
            iconIV[0].setImageResource(iconNum)
        }

        clickCL[0].setOnClickListener {

            val tel = SharedPreferencesUtil.instances.getAccountId()

            if (tel.isNullOrBlank()) {
                toFragment("")
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    getElementData(tel)
                }
                toFragment(tel.toString())
            }
        }


        bookViewModel.careList.observe(viewLifecycleOwner) {

            it?.let {

                var index: Int
                statusList.clear()
                memberIdList.clear()

                for (i in it.indices) {

                    index = i + 1
                    legendTV[index].text = it[i].nick_name.toString()

                    if (backgroundNum != 0) {
                        backgroundV[index].background = ResourcesCompat.getDrawable(
                            resources, backgroundNum, null
                        )
                    }

                    if (iconNum != 0) {
                        iconIV[index].setImageResource(iconNum)
                    }

                    statusList.add(it[i].status.toString())
                    memberIdList.add(it[i].cmember_id.toString())

                    // 讀取指定帳號
                    clickCL[index].setOnClickListener {

                        // 有授權
                        if (statusList[i] == "1") {

                            CoroutineScope(Dispatchers.IO).launch {
                                getElementData(memberIdList[i])
                            }

                            toFragment(memberIdList[i])

                        } else {

                            DialogUtils.showSingle(requireActivity(), "", "對方未授權") {}
                        }
                    }

                    if (i == 7) {
                        return@observe
                    }
                }
            }
        }
    }

    private suspend fun getElementData(memberId: String) {

        val request = ElementRequest(
            memberId,
            DateUtil.instance.ago3MonthYmdHms(),
            DateUtil.instance.currentYmdHms()
        )

        when (legendStr) {

            // 10. App 取得心率數值
            getString(R.string.circle_heart_rate) -> watchViewModel.getHeartRate(request)
            // 11. App 取得血壓數值
            getString(R.string.circle_blood_pressure) -> watchViewModel.getBp(request)
            // 13. App 取得血氧數值
            getString(R.string.circle_blood_oxygen) -> watchViewModel.getOxygen(request)
            // 17. App 取得呼吸率數值
            getString(R.string.circle_breath_rate) -> watchViewModel.getBreathRate(request)
            // 19. App 取得體溫數值
            getString(R.string.circle_body_temperature) -> watchViewModel.getTemperature(request)
            // 08. App 取得睡眠數據
            getString(R.string.circle_sleep) -> watchViewModel.getSleep(request)
            // 07. App 取得步數資料
            getString(R.string.circle_step_count) -> watchViewModel.getGetSteps(request)
            // 15. App 取得ECG數值
            getString(R.string.circle_ecg) -> watchViewModel.getECG(request)

            getString(R.string.circle_kcal) -> {
                CoroutineScope(Dispatchers.IO).launch {

                    // 07. App 取得步數資料
                    val step = async { watchViewModel.getGetSteps(request) }
                    step.await()
                    // 28. App 取得卡路里的數值
                    watchViewModel.getKcal(request)
                }
            }
            // 26.App 取得雙臂血壓數值
            getString(R.string.circle_arm_blood_pressure) -> watchViewModel.getBp2(request)
            // 24.App 取得黃斑部色素檢查數值
            getString(R.string.circle_macular_pigment) -> watchViewModel.getMPOD(request)
            // 22.App 取得骨密的檢查數值
            getString(R.string.circle_bone_density) -> watchViewModel.getBMD(request)
        }
    }

    private fun toFragment(tel: String) {

        when (legendStr) {

            getString(R.string.circle_heart_rate) ->
                findNavController().navigate(
                    CFragmentDirections.actionCFragmentToHeartRateFragment(tel)
                )
            getString(R.string.circle_blood_pressure) ->
                findNavController().navigate(
                    CFragmentDirections.actionCFragmentToBloodPressureFragment(tel)
                )
            getString(R.string.circle_blood_oxygen) ->
                findNavController().navigate(
                    CFragmentDirections.actionCFragmentToBloodOxygenFragment(tel)
                )

            getString(R.string.circle_breath_rate) ->
                findNavController().navigate(
                    CFragmentDirections.actionCFragmentToBreathRateFragment(tel)
                )
            getString(R.string.circle_body_temperature) ->
                findNavController().navigate(
                    CFragmentDirections.actionCFragmentToBodyTemperatureFragment(tel)
                )
            getString(R.string.circle_sleep) ->
                findNavController().navigate(
                    CFragmentDirections.actionCFragmentToSleepFragment(tel)
                )

            getString(R.string.circle_step_count) ->
                findNavController().navigate(
                    CFragmentDirections.actionCFragmentToStepCountFragment(tel)
                )
            getString(R.string.circle_ecg) ->
                findNavController().navigate(
                    CFragmentDirections.actionCFragmentToEcgFragment(tel)
                )
            getString(R.string.circle_kcal) ->
                findNavController().navigate(
                    CFragmentDirections.actionCFragmentToKcalFragment(tel)
                )

            getString(R.string.circle_arm_blood_pressure) -> {
                findNavController().navigate(
                    CFragmentDirections.actionCFragmentToArmBloodPressureFragment(tel)
                )
            }
            getString(R.string.circle_macular_pigment) -> {
                findNavController().navigate(
                    CFragmentDirections.actionCFragmentToMacularPigmentFragment(tel)
                )
            }
            getString(R.string.circle_bone_density) ->
                findNavController().navigate(
                    CFragmentDirections.actionCFragmentToBoneDensityFragment(tel)
                )
        }
    }
}