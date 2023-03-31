package com.jotangi.NumberHealthy.ui.home.watch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.NumberHealthy.api.watch.GetKcalDataBean
import com.jotangi.NumberHealthy.databinding.AdapterKcalBinding
import com.jotangi.NumberHealthy.utils.DateUtil

class KcalAdapter(private val list: List<GetKcalDataBean>) :
    RecyclerView.Adapter<KcalAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: AdapterKcalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bean: GetKcalDataBean) {

            binding.apply {

                with(bean) {

                    tvValue.text = KCAL
                    tvDate.text = DateUtil.instance.clipTimeToYMD(startTime)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterKcalBinding.inflate(
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