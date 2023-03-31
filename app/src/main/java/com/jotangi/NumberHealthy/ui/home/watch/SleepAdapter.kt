package com.jotangi.NumberHealthy.ui.home.watch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.NumberHealthy.api.watch.SleepDayTotalData
import com.jotangi.NumberHealthy.databinding.AdapterSleepBinding
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.smartwatch.WatchUtils


class SleepAdapter(private val list: List<SleepDayTotalData>) :
    RecyclerView.Adapter<SleepAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: AdapterSleepBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bean: SleepDayTotalData) {

            val total = bean.deepMinuteTotal + bean.lightMinuteTotal

            binding.apply {

                tvSleepHr.text = (total / 60).toString()
                tvSleepMin.text = (total % 60).toString()
                tvSleepDate.text = DateUtil.instance.clipTimeFormatSecond(
                    bean.startTime
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterSleepBinding.inflate(
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