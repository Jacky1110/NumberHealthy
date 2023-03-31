package com.jotangi.NumberHealthy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.databinding.FragmentMacularPigmentDetailBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding

class MacularPigmentDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentMacularPigmentDetailBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private lateinit var macularPigmentDetailAdapter: MacularPigmentDetailAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMacularPigmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {

        setToolbarArrow("黃斑部色素紀錄")

        watchViewModel.getMpodList.observe(viewLifecycleOwner) { beanList ->

            macularPigmentDetailAdapter = MacularPigmentDetailAdapter(beanList)

            binding.rvBpod.apply {

                layoutManager = LinearLayoutManager(context)
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                adapter = macularPigmentDetailAdapter
            }
        }
    }
}