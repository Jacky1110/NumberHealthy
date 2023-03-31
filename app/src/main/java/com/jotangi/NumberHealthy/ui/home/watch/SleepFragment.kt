package com.jotangi.NumberHealthy.ui.home.watch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.chart.LineSleep
import com.jotangi.NumberHealthy.databinding.FragmentSleepBinding
import com.jotangi.NumberHealthy.ui.home.bar.HorizontalColorBar
import com.jotangi.NumberHealthy.utils.DateUtil


class SleepFragment : BaseFragment() {

    private lateinit var binding: FragmentSleepBinding
    override fun getToolBar() = binding.toolbar

    private val args by navArgs<SleepFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSleepBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initCallBack()
    }

    private fun initView() {

        setToolbarDetail(
            getString(R.string.circle_sleep),
            SleepFragmentDirections.actionSleepFragmentToSleepDetailFragment(args.tel)
        )

        binding.apply {

            hcbvSleep.setType(HorizontalColorBar.Type.SLEEP)
            LineSleep.instance.init(lc)
        }
    }

    private fun initCallBack() {

        watchViewModel.sleepDayTotalDataLD.observe(viewLifecycleOwner) { list ->

            if (list.isNullOrEmpty()) return@observe

            binding.apply {

                with(list.first()) {

                    tvDate.text = DateUtil.instance.clipTimeFormatSecond(startTime)

                    val totalMinute = deepMinuteTotal + lightMinuteTotal
                    tvValueHour.text = (totalMinute / 60).toString()
                    tvValueMinute.text = (totalMinute % 60).toString()

                    val dayMinute = DateUtil.instance.intervalMinute(startTime, endTime)
                    val quality = ((totalMinute.toFloat() / dayMinute) * 100).toInt()
//                    val quality = (deepMinuteTotal / totalMinute.toFloat() * 100).toInt()
                    tvValueQuality.text = quality.toString()
                    hcbvSleep.setDataValue(quality)

                    val lineList = DateUtil.instance.sleepLineList(list)
                    Log.d(TAG, "lineList: $lineList")
                    LineSleep.instance.setValue(lineList, lc)
                }
            }
        }

        watchViewModel.sleepHorViewLD.observe(viewLifecycleOwner) {

            if (it.isNullOrEmpty()) return@observe

            watchViewModel.sleepDayTotalDataLD.value?.first()?.apply {

                binding.shcbvSleep.setDatalist(
                    it,
                    startTime,
                    endTime
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        watchViewModel.sleepDayTotalDataLD.postValue(listOf())
        watchViewModel.sleepHorViewLD.postValue(listOf())
    }
}