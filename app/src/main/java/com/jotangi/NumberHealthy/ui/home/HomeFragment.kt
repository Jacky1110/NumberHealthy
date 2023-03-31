package com.jotangi.NumberHealthy.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.databinding.FragmentHomeBinding
import com.jotangi.NumberHealthy.utils.CommonKtUtils
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.jotangi.NumberHealthy.utils.smartwatch.WatchUtils
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    override fun getToolBar() = binding.toolbar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
//        initHandler()
        initCallBack()
    }

    private fun initView() {

        setToolbar("數體健")

        binding.apply {
            if (SharedPreferencesUtil.instances.getAccountId() == "53651543") {
                findNavController().navigate(R.id.storeMangerFragment)
            } else {
                viewPager2.adapter = ViewPagerAdapter(this@HomeFragment)
                ciBottomCircle.setViewPager(viewPager2)
            }
        }
    }

    private fun initHandler() {

        binding.apply {

            ivPicture.setOnClickListener {
                watchViewModel.setUpDataTime(
                    DateUtil.instance.after3MinuteYmdHms()
                )
            }

            tv.setOnClickListener {
                Log.d(TAG, "time: ${watchViewModel.upDataTime.value}")
            }
        }
    }

    private fun initCallBack() {

        bookViewModel.playStoreVersion.observe(viewLifecycleOwner) {

            Log.d(TAG, "playStoreVersion: $it")
            CommonKtUtils.instance.checkVersion(it) {

                showUpVersion()
            }
        }

        SharedPreferencesUtil.instances.getAccountId().let {

            if (!it.isNullOrBlank()) {

                watchViewModel.isBleConnect.observe(viewLifecycleOwner) { boolean ->
                    setToolbarWatchStatus(boolean)
                }
            }
        }

        bookViewModel.messageBoxCountResult.observe(viewLifecycleOwner) { result ->
            if (result != null && result.isNotEmpty()) {
                setupToolbarWithCount(result)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (!SharedPreferencesUtil.instances.getAccountId().isNullOrBlank()) {

            lifecycleScope.launch {

                bookViewModel.getMessageBoxCount()
            }
        }
    }
}