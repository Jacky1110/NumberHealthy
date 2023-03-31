package com.jotangi.NumberHealthy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.api.watch.ElementRequest
import com.jotangi.NumberHealthy.api.watch.UploadMpodRequest
import com.jotangi.NumberHealthy.databinding.FragmentMacularPigmentBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.utils.AppUtil
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.DialogDateUtil
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.jotangi.NumberHealthy.api.watch.WatchApiRepository
import com.jotangi.NumberHealthy.chart.LineMacular
import kotlinx.coroutines.launch

class MacularPigmentFragment : BaseFragment() {

    private lateinit var binding: FragmentMacularPigmentBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private val args by navArgs<MacularPigmentFragmentArgs>()
    private val apiRepository: WatchApiRepository by lazy { WatchApiRepository() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMacularPigmentBinding.inflate(inflater, container, false)
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
            getString(R.string.circle_macular_pigment),
            R.id.macularPigmentDetailFragment
        )

        if (AppUtil.instance.checkSelf(
                SharedPreferencesUtil.instances.getAccountId(),
                args.tel
            )
        ) binding.btInsertData.visibility = View.GONE

        LineMacular.instance.init(binding.lc)
    }

    private fun initHandler() {

        binding.apply {

            btInsertData.setOnClickListener {

                DialogDateUtil.instance.macularPigment(
                    requireActivity(),
                    watchViewModel.getMpodList.value,
                    request = { refreshData(it) }
                )
            }
        }
    }

    private fun refreshData(request: UploadMpodRequest) {

        lifecycleScope.launch {

            val response = apiRepository.uploadMpod(request)
            if (response.code != "0x0200") return@launch

            watchViewModel.getMPOD(
                ElementRequest(
                    SharedPreferencesUtil.instances.getAccountId() ?: "",
                    DateUtil.instance.ago3MonthYmdHms(),
                    DateUtil.instance.currentYmdHms()
                )
            )
        }
    }

    private fun initCallBack() {

        watchViewModel.getMpodList.observe(viewLifecycleOwner) { beanList ->

            if (beanList.isEmpty()) return@observe

            binding.apply {

                tvDate.text = DateUtil.instance.clipTimeFormatSecond(
                    beanList[0].mpodStartTime
                )
                tvValue.text = "${beanList[0].lefteye}(L) / ${beanList[0].righteye}(R)"
                cbBpodLeftEye.initLegend()
                cbBpodLeftEye.setDataValue(beanList[0].lefteye)
                cbBpodRightEye.setDataValue(beanList[0].righteye)

                LineMacular.instance.setValue(beanList, lc)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        watchViewModel.getMpodList.postValue(listOf())
    }
}