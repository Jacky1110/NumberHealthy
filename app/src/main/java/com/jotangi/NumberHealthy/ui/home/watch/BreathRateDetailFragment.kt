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
import com.jotangi.NumberHealthy.api.watch.BreathRateBean
import com.jotangi.NumberHealthy.databinding.FragmentBreathRateDetailBinding
import com.jotangi.NumberHealthy.api.watch.WatchApiRepository

class BreathRateDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentBreathRateDetailBinding
    override fun getToolBar() = binding.toolbar

    private val args by navArgs<BreathRateDetailFragmentArgs>()
    protected val apiRepository: WatchApiRepository by lazy { WatchApiRepository() }

    private lateinit var breathRateAdapter: BreathRateAdapter
    private val dataList = arrayListOf<BreathRateBean>()
    var month: Int = 0
    var year: Int = 0
    var minute: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBreathRateDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initCallBack()
    }

    private fun initView() {

        setToolbarArrowTrend(
            getString(R.string.circle_breath_rate),
            args.tel,
            BreathRateDetailFragmentDirections.actionBreathRateDetailFragmentToWebFragment(
                getString(R.string.circle_breath_rate)
            )
        )

        breathRateAdapter = BreathRateAdapter(dataList)
        binding.breathRateRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = breathRateAdapter
        }
    }

    private fun initCallBack() {

        watchViewModel.lastBreathRateBean.observe(viewLifecycleOwner) { datas ->
            dataList.clear()
            dataList.addAll(datas)
            breathRateAdapter.notifyDataSetChanged()
        }
    }
}