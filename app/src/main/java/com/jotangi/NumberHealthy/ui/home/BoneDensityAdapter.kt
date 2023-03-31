package com.jotangi.NumberHealthy.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.NumberHealthy.api.watch.GetBmdBean
import com.jotangi.NumberHealthy.databinding.AdapterBoneDensityBinding
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.smartwatch.WatchUtils

class BoneDensityAdapter(private val list: List<GetBmdBean>) :
    RecyclerView.Adapter<BoneDensityAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: AdapterBoneDensityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bean: GetBmdBean) {

            binding.apply {
                tvNum.text = bean.TScore
                tvDate.text = DateUtil.instance.clipTimeFormatSecond(bean.startTime)
                tvInputType.text = WatchUtils.instance.inputType(bean.dataType.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterBoneDensityBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}