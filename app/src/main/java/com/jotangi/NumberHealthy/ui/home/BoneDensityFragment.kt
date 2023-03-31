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
import com.jotangi.NumberHealthy.api.watch.UploadBmdRequest
import com.jotangi.NumberHealthy.databinding.FragmentBoneDensityBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.api.watch.WatchApiRepository
import com.jotangi.NumberHealthy.chart.BarBoneDensity
import com.jotangi.NumberHealthy.utils.*
import kotlinx.coroutines.launch

class BoneDensityFragment : BaseFragment() {

    private lateinit var binding: FragmentBoneDensityBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private val args by navArgs<BoneDensityFragmentArgs>()
    private val apiRepository: WatchApiRepository by lazy { WatchApiRepository() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBoneDensityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initHandler()
        initCallBack()
    }

    private fun initView() {

        setToolbarDetail(getString(R.string.circle_bone_density), R.id.boneDensityDetailFragment)

        binding.apply {

            BarBoneDensity.instance.init(bc)

            if (AppUtil.instance.checkSelf(
                    SharedPreferencesUtil.instances.getAccountId(),
                    args.tel
                )
            ) btInsertData.visibility = View.GONE
        }
    }

    private fun initHandler() {

        binding.apply {

            btInsertData.setOnClickListener {

                DialogDateUtil.instance.bone(
                    requireActivity(),
                    watchViewModel.getBmdList.value,
                    request = { refreshData(it) }
                )
            }
        }
    }

    private fun refreshData(request: UploadBmdRequest) {

        lifecycleScope.launch {

            val response = apiRepository.uploadBmd(request)
            if (response.code != "0x0200") return@launch

            watchViewModel.getBMD(
                ElementRequest(
                    SharedPreferencesUtil.instances.getAccountId() ?: "",
                    DateUtil.instance.ago3MonthYmdHms(),
                    DateUtil.instance.currentYmdHms()
                )
            )
        }
    }

    private fun initCallBack() {

        watchViewModel.getBmdList.observe(viewLifecycleOwner) { beanList ->

            if (beanList.isNullOrEmpty()) return@observe

            binding.apply {

                tvDate.text = DateUtil.instance.clipTimeFormatSecond(
                    beanList[0].startTime
                )
                tvValue.text = MathUtil.instance.fixBoneValue(beanList[0].TScore)
                cbBoneDensity.setDataValue(beanList[0].TScore)

                BarBoneDensity.instance.setValue(beanList, bc)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        watchViewModel.getBmdList.postValue(listOf())
    }
}