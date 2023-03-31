package com.jotangi.NumberHealthy.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.NumberHealthy.api.watch.GetMpodBean
import com.jotangi.NumberHealthy.databinding.AdapterMacularPigmentBinding
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.smartwatch.WatchUtils

class MacularPigmentDetailAdapter(private val list: List<GetMpodBean>) :
    RecyclerView.Adapter<MacularPigmentDetailAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: AdapterMacularPigmentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bean: GetMpodBean) {

            binding.apply {

                tvNum.text = "${bean.lefteye}(L) / ${bean.righteye}(R)"
                tvDate.text = DateUtil.instance.clipTimeFormatSecond(bean.mpodStartTime)
                tvInputType.text = WatchUtils.instance.inputType(bean.dataType.toString())
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MacularPigmentDetailAdapter.ViewHolder {
        return ViewHolder(
            AdapterMacularPigmentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: MacularPigmentDetailAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}