package com.jotangi.NumberHealthy.ui.home.watch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.chart.BarBreathRate
import com.jotangi.NumberHealthy.databinding.FragmentBreathRateBinding
import com.jotangi.NumberHealthy.utils.DateUtil

class BreathRateFragment : BaseFragment() {

    private lateinit var binding: FragmentBreathRateBinding
    override fun getToolBar() = binding.toolbar

    private val args by navArgs<BreathRateFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBreathRateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initCallBack()
    }

    private fun initView() {

        setToolbarDetail(
            getString(R.string.circle_breath_rate),
            BreathRateFragmentDirections.actionBreathRateFragmentToBreathRateDetailFragment(
                args.tel
            )
        )

        BarBreathRate.instance.init(binding.bc)
    }

    private fun initCallBack() {

        watchViewModel.lastBreathRateBean.observe(viewLifecycleOwner) {

            if (it.isNullOrEmpty()) return@observe

            binding.apply {

                tvValue.text = it[0].respiratoryrate
                tvDate.text = DateUtil.instance.clipTimeFormatSecond(
                    it[0].startTime
                )

                BarBreathRate.instance.setValue(it, bc)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        watchViewModel.lastBreathRateBean.postValue(listOf())
    }
}