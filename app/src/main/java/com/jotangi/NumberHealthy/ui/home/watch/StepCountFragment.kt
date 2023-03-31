package com.jotangi.NumberHealthy.ui.home.watch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.chart.LineStep
import com.jotangi.NumberHealthy.databinding.FragmentStepCountBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.utils.DateUtil

class StepCountFragment : BaseFragment() {

    private lateinit var binding: FragmentStepCountBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private val args by navArgs<StepCountFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStepCountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initCallBack()
    }

    private fun initView() {

        setToolbarDetail(
            getString(R.string.circle_step_count),
            StepCountFragmentDirections.actionStepCountFragmentToStepCountDetailFragment(
                args.tel
            )
        )

        LineStep.instance.init(binding.lc)
    }

    private fun initCallBack() {

        binding.apply {

            watchViewModel.dayFootStepsData.observe(viewLifecycleOwner) {

                if (it.isNullOrEmpty()) return@observe

                tvDate.text = DateUtil.instance.clipTimeToYMD(it.first().sportStartTime)
                tvValue.text = it.first().sportStep
                tvDistance.text = "${it.first().sportDistance}米"
                tvCard.text = "${it.first().sportCalorie}千卡"
                tvMinute.text =
                    "${
                        DateUtil.instance.intervalMinute(
                            it.first().sportStartTime, it.first().sportEndTime
                        )
                    }分鐘"

                LineStep.instance.setValue(it, lc)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        watchViewModel.dayFootStepsData.postValue(listOf())
    }
}