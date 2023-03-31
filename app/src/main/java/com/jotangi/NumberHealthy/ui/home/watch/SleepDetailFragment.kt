package com.jotangi.NumberHealthy.ui.home.watch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.api.watch.SleepDayTotalData
import com.jotangi.NumberHealthy.databinding.FragmentSleepDetailBinding


class SleepDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentSleepDetailBinding
    override fun getToolBar() = binding.toolbar

    private val args by navArgs<SleepDetailFragmentArgs>()

    private val dataList = arrayListOf<SleepDayTotalData>()
    private lateinit var sleepAdapter: SleepAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSleepDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initCallBack()
    }

    private fun initView() {

        setToolbarArrowTrend(
            getString(R.string.circle_sleep),
            args.tel,
            SleepDetailFragmentDirections.actionSleepDetailFragmentToWebFragment(
                getString(R.string.circle_sleep)
            )
        )

        sleepAdapter = SleepAdapter(dataList)
        binding.rvSleep.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = sleepAdapter
        }
    }

    private fun initCallBack() {

        watchViewModel.sleepDayTotalDataLD.observe(viewLifecycleOwner) {
            dataList.clear()
            dataList.addAll(it)
            sleepAdapter.notifyDataSetChanged()
        }
    }
}