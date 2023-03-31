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
import com.jotangi.NumberHealthy.api.watch.TemperatureBean
import com.jotangi.NumberHealthy.databinding.FragmentBodyTemperatureDetailBinding

class BodyTemperatureDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentBodyTemperatureDetailBinding
    override fun getToolBar() = binding.toolbar

    private val args by navArgs<BodyTemperatureDetailFragmentArgs>()

    private lateinit var temperatureAdapter: BodyTemperatureAdapter
    private val dataList = arrayListOf<TemperatureBean>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBodyTemperatureDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarArrowTrend(
            getString(R.string.circle_body_temperature),
            args.tel,
            BodyTemperatureDetailFragmentDirections.actionBodyTemperatureDetailFragmentToWebFragment(
                getString(R.string.circle_body_temperature)
            )
        )

        temperatureAdapter = BodyTemperatureAdapter(dataList)
        binding.temperatureRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = temperatureAdapter
        }

        watchViewModel.lastTemperatureLD.observe(viewLifecycleOwner) { datas ->
            dataList.clear()
            dataList.addAll(datas)
            temperatureAdapter.notifyDataSetChanged()
        }
    }
}