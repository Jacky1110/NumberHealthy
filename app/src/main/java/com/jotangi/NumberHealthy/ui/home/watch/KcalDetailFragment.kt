package com.jotangi.NumberHealthy.ui.home.watch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.api.watch.GetKcalDataBean
import com.jotangi.NumberHealthy.databinding.FragmentKcalDetailBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding

class KcalDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentKcalDetailBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private var arrayList = ArrayList<GetKcalDataBean>()
    private lateinit var kcalDetailAdapter: KcalAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentKcalDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initCallBack()
    }

    private fun initView() {

        setToolbarArrow("卡路里紀錄")

        kcalDetailAdapter = KcalAdapter(arrayList)
        binding.rv.apply {

            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = kcalDetailAdapter
        }
    }

    private fun initCallBack() {
        watchViewModel.getKcalList.observe(viewLifecycleOwner) {
            it?.let { list ->
                arrayList.clear()
                arrayList.addAll(list)
                kcalDetailAdapter.notifyDataSetChanged()
            }
        }
    }
}