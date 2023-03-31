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
import com.jotangi.NumberHealthy.api.watch.OxygenBean
import com.jotangi.NumberHealthy.databinding.FragmentBloodOxygenDetailBinding


class BloodOxygenDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentBloodOxygenDetailBinding
    override fun getToolBar() = binding.toolbar

    private val args by navArgs<BloodOxygenFragmentArgs>()

    private lateinit var oxyAdapter: BloodOxygenAdapter
    private val dataList = arrayListOf<OxygenBean>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBloodOxygenDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initCallBack()
    }

    private fun initView() {

        setToolbarArrow("血氧測量紀錄")
        setToolbarArrowTrend(
            getString(R.string.circle_blood_oxygen),
            args.tel,
            BloodOxygenDetailFragmentDirections.actionBloodOxygenDetailFragmentToWebFragment(
                getString(R.string.circle_blood_oxygen)
            )
        )

        oxyAdapter = BloodOxygenAdapter(dataList)
        binding.rvOxy.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = oxyAdapter
        }
    }

    private fun initCallBack() {

        watchViewModel.lastOxygenLD.observe(viewLifecycleOwner) { datas ->
            dataList.clear()
            dataList.addAll(datas)
            oxyAdapter.notifyDataSetChanged()
        }
    }
}