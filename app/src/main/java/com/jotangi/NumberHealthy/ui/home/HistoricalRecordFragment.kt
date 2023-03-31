package com.jotangi.NumberHealthy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.NumberHealthy.api.book.GetStoreApplyListBeen
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.databinding.FragmentHistoricalRecordBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding


class HistoricalRecordFragment : BaseFragment() {

    private lateinit var binding: FragmentHistoricalRecordBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar


    private val historyList = arrayListOf<GetStoreApplyListBeen>()
    private lateinit var historicalRecordAdapter: HistoricalRecordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoricalRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initCallBack()
    }


    private fun initView() {
        setToolbarArrow("歷史紀錄")
        historicalRecordAdapter = HistoricalRecordAdapter(historyList)
        binding.historyRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = historicalRecordAdapter
        }

    }

    private fun initCallBack() {
        bookViewModel.getHistoryList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.apply {
                    historyList.clear()
                    historyList.addAll(it)
                    historicalRecordAdapter.notifyDataSetChanged()
                }
            }
        }
    }

}