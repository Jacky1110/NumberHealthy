package com.jotangi.NumberHealthy.ui.home.watch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.api.watch.ElementRequest
import com.jotangi.NumberHealthy.api.watch.UploadHrRequest
import com.jotangi.NumberHealthy.api.watch.WatchApiRepository
import com.jotangi.NumberHealthy.chart.BarHeartRate
import com.jotangi.NumberHealthy.databinding.FragmentHeartRateBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.utils.AppUtil
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.DialogDateUtil
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import kotlinx.coroutines.launch

class HeartRateFragment : BaseFragment() {

    private lateinit var binding: FragmentHeartRateBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private val args by navArgs<HeartRateFragmentArgs>()
    private val apiRepository: WatchApiRepository by lazy { WatchApiRepository() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeartRateBinding.inflate(inflater, container, false)
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
            getString(R.string.circle_heart_rate),
            HeartRateFragmentDirections
                .actionHeartRateFragmentToHeartRateDetailFragment(args.tel)
        )

        binding.apply {

            BarHeartRate.instance.init(binding.bc)

            if (AppUtil.instance.checkSelf(
                    SharedPreferencesUtil.instances.getAccountId(),
                    args.tel
                )
            ) btnInsertData.visibility = View.GONE
        }
    }

    private fun initHandler() {

        binding.btnInsertData.setOnClickListener {

            DialogDateUtil.instance.heartRate(
                requireActivity(),
                request = { refreshData(it) }
            )
        }
    }

    private fun refreshData(request: UploadHrRequest) {

        lifecycleScope.launch {

            val response = apiRepository.uploadHr(request)
            if (response.code != "0x0200") return@launch

            watchViewModel.getHeartRate(
                ElementRequest(
                    SharedPreferencesUtil.instances.getAccountId() ?: "",
                    DateUtil.instance.ago3MonthYmdHms(),
                    DateUtil.instance.currentYmdHms()
                )
            )
        }
    }

    private fun initCallBack() {

        watchViewModel.lastHeartRateBean.observe(viewLifecycleOwner) { list ->

            if (list.isNullOrEmpty()) return@observe

            binding.apply {

                tvValue.text = list[0].heartValue
                tvDate.text =
                    DateUtil.instance.clipTimeFormatSecond(list[0].heartStartTime)
                heartRateColorBar.setDataValue(list[0].heartValue?.toInt() ?: 0)

                BarHeartRate.instance.setValue(list, binding.bc, requireContext())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        watchViewModel.lastHeartRateBean.postValue(listOf())
    }
}