package com.jotangi.NumberHealthy.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.NumberHealthy.api.watch.GetBp2Bean
import com.jotangi.NumberHealthy.databinding.AdapterArmBloodPressureDetailBinding
import com.jotangi.NumberHealthy.utils.DateUtil

class ArmBloodPressureDetailAdapter(private val list: List<GetBp2Bean>) :
    RecyclerView.Adapter<ArmBloodPressureDetailAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: AdapterArmBloodPressureDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bean: GetBp2Bean) {
            binding.apply {

                with(bean) {

                    tvHrValue.text = heartValue
                    tvTime.text = DateUtil.instance.clipTimeFormatSecond(bloodStartTime)
                    when (dataType) {
                        "0" -> {
                            tvType.text = "手錶數值"
                        }
                        "1" -> {
                            tvType.text = "手動輸入數據"
                        }
                        else -> {
                            tvType.text = "醫事人員輸入數據"
                        }
                    }

                    etLSys.text = LbloodSBP
                    etRSys.text = RbloodSBP
                    etLDia.text = LbloodDBP
                    etRDia.text = RbloodDBP
                    etLPp.text = LbloodPP
                    etRPp.text = RbloodPP
                    etLMap.text = LbloodMAP
                    etRMap.text = RbloodMAP
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterArmBloodPressureDetailBinding.inflate(
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