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
import com.jotangi.NumberHealthy.api.watch.UploadOxygenRequest
import com.jotangi.NumberHealthy.api.watch.WatchApiRepository
import com.jotangi.NumberHealthy.chart.BarBloodOxygen
import com.jotangi.NumberHealthy.databinding.FragmentBloodOxygenBinding
import com.jotangi.NumberHealthy.ui.home.BoneDensityFragmentArgs
import com.jotangi.NumberHealthy.utils.AppUtil
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.DialogDateUtil
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import kotlinx.coroutines.launch


class BloodOxygenFragment : BaseFragment() {

    private lateinit var binding: FragmentBloodOxygenBinding
    override fun getToolBar() = binding.toolbar

    private val args by navArgs<BoneDensityFragmentArgs>()
    private val apiRepository: WatchApiRepository by lazy { WatchApiRepository() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBloodOxygenBinding.inflate(inflater, container, false)
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
            getString(R.string.circle_blood_oxygen),
            BloodOxygenFragmentDirections
                .actionBloodOxygenFragmentToBloodOxygenDetailFragment(args.tel)
        )

        binding.apply {

            BarBloodOxygen.instance.init(bc)

            if (AppUtil.instance.checkSelf(
                    SharedPreferencesUtil.instances.getAccountId(),
                    args.tel
                )
            ) btnInsertData.visibility = View.GONE
        }
    }

    private fun initHandler() {

        binding.btnInsertData.setOnClickListener {

            DialogDateUtil.instance.bloodOxygen(
                requireActivity(),
                request = { refreshData(it) }
            )
        }
    }

    private fun refreshData(request: UploadOxygenRequest) {

        lifecycleScope.launch {

            val resp = apiRepository.uploadOxygen(request)
            if (resp.code != "0x0200") return@launch

            watchViewModel.getOxygen(
                ElementRequest(
                    SharedPreferencesUtil.instances.getAccountId() ?: "",
                    DateUtil.instance.ago3MonthYmdHms(),
                    DateUtil.instance.currentYmdHms()
                )
            )
        }
    }

    private fun initCallBack() {

        watchViewModel.lastOxygenLD.observe(viewLifecycleOwner) {

            if (it.isNullOrEmpty()) return@observe

            binding.apply {

                tvDate.text = DateUtil.instance.clipTimeFormatSecond(it[0].startTime)
                tvValue.text = it[0].OOValue
                colorBar.setDataValue(it[0].OOValue?.toInt() ?: 0)

                BarBloodOxygen.instance.setValue(it, bc, requireContext())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        watchViewModel.lastOxygenLD.postValue(listOf())
    }
}