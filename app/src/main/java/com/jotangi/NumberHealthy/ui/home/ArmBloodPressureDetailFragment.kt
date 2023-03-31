package com.jotangi.NumberHealthy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.api.watch.GetBp2Bean
import com.jotangi.NumberHealthy.databinding.FragmentArmBloodPressureDetailBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding

class ArmBloodPressureDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentArmBloodPressureDetailBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private lateinit var armBloodPressureDetailAdapter: ArmBloodPressureDetailAdapter
    private var arrayList = ArrayList<GetBp2Bean>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArmBloodPressureDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initCallBack()
    }

    private fun initView() {

        setToolbarArrow("雙臂血壓紀錄")

        armBloodPressureDetailAdapter = ArmBloodPressureDetailAdapter(arrayList)
        binding.rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = armBloodPressureDetailAdapter
        }
    }

    private fun initCallBack() {
        watchViewModel.getBp2List.observe(viewLifecycleOwner) {
            it?.let { list ->
                arrayList.clear()
                arrayList.addAll(list)
                armBloodPressureDetailAdapter.notifyDataSetChanged()
            }
        }
    }
}