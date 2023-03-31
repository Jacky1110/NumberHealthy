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
import com.jotangi.NumberHealthy.api.watch.UploadKcalRequest
import com.jotangi.NumberHealthy.api.watch.WatchApiRepository
import com.jotangi.NumberHealthy.chart.BarKcal
import com.jotangi.NumberHealthy.databinding.FragmentKcalBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.utils.AppUtil
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.DialogDateUtil
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class KcalFragment : BaseFragment() {

    private lateinit var binding: FragmentKcalBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private val args by navArgs<KcalFragmentArgs>()
    private val apiRepository: WatchApiRepository by lazy { WatchApiRepository() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKcalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initHandler()
        initCallBack()
    }

    private fun initView() {

        setToolbarDetail(getString(R.string.circle_kcal), R.id.kcalDetailFragment)

        BarKcal.instance.init(binding.bc)

        if (AppUtil.instance.checkSelf(
                SharedPreferencesUtil.instances.getAccountId(),
                args.tel
            )
        ) binding.btInsertData.visibility = View.GONE
    }

    private fun initHandler() {

        binding.btInsertData.setOnClickListener {

            DialogDateUtil.instance.kcal(
                requireActivity(),
                request = { refreshData(it) }
            )
        }
    }

    private fun refreshData(request: UploadKcalRequest) {

        lifecycleScope.launch {

            val response = apiRepository.uploadKcal(request)
            if (response.code != "0x0200") return@launch

            val request = ElementRequest(
                SharedPreferencesUtil.instances.getAccountId() ?: "",
                DateUtil.instance.ago3MonthYmdHms(),
                DateUtil.instance.currentYmdHms()
            )

            val step = async { watchViewModel.getGetSteps(request) }
            step.await()
            watchViewModel.getKcal(request)
        }
    }

    private fun initCallBack() {

        watchViewModel.getKcalList.observe(viewLifecycleOwner) {

            if (it.isNullOrEmpty()) return@observe

            binding.apply {

                tvDate.text = DateUtil.instance.clipTimeToYMD(it.first().startTime)
                tvValue.text = it.first().KCAL
                BarKcal.instance.setValue(it, bc)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        watchViewModel.dayFootStepsData.postValue(listOf())
        watchViewModel.getKcalList.postValue(listOf())
    }
}