package com.jotangi.NumberHealthy.ui.home.watch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.chart.BarBodyTemperature
import com.jotangi.NumberHealthy.databinding.FragmentBodyTemperatureBinding
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.smartwatch.WatchUtils

class BodyTemperatureFragment : BaseFragment() {

    private lateinit var binding: FragmentBodyTemperatureBinding
    override fun getToolBar() = binding.toolbar

    private val args by navArgs<BodyTemperatureFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBodyTemperatureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initCallBack()
    }

    private fun initView() {

        setToolbarDetail(
            getString(R.string.circle_body_temperature),
            BodyTemperatureFragmentDirections.actionBodyTemperatureFragmentToBodyTemperatureDetailFragment(
                args.tel
            )
        )

        BarBodyTemperature.instance.init(binding.bc)
    }

    private fun initCallBack() {

        watchViewModel.lastTemperatureLD.observe(viewLifecycleOwner) {

            if (it.isNullOrEmpty()) return@observe

            binding.apply {

                tvValue.text = WatchUtils.instance.fixDecimalOne(it[0].temperature.toFloat())
                tvDate.text = DateUtil.instance.clipTimeFormatSecond(it[0].startTime)
                BarBodyTemperature.instance.setValue(it, bc)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        watchViewModel.lastTemperatureLD.postValue(listOf())
    }
}