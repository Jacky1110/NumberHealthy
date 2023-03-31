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
import com.jotangi.NumberHealthy.api.watch.BPBean
import com.jotangi.NumberHealthy.databinding.FragmentBloodPressureDetailBinding


class BloodPressureDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentBloodPressureDetailBinding
    override fun getToolBar() = binding.toolbar

    private val args by navArgs<BloodPressureFragmentArgs>()

    private lateinit var bpAdapter: BloodPressureAdapter
    private val dataList = arrayListOf<BPBean>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBloodPressureDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initCallBack()
    }

    private fun initView() {

        setToolbarArrowTrend(
            getString(R.string.circle_blood_pressure),
            args.tel,
            BloodPressureDetailFragmentDirections.actionBloodPressureDetailFragmentToWebFragment(
                getString(R.string.circle_blood_pressure)
            )
        )

        bpAdapter = BloodPressureAdapter(dataList)
        binding.rvBP.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = bpAdapter
        }
    }

    private fun initCallBack() {

        watchViewModel.lastBpBean.observe(viewLifecycleOwner) {
            dataList.clear()
            dataList.addAll(it)
            bpAdapter.notifyDataSetChanged()
        }
    }
}