package com.jotangi.NumberHealthy.ui.home.watch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.api.watch.OxygenBean
import com.jotangi.NumberHealthy.utils.DateUtil

class BloodOxygenAdapter(private val mData: List<OxygenBean>) :
    RecyclerView.Adapter<BloodOxygenAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BloodOxygenAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_blood_oxygen, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvNum: TextView = v.findViewById(R.id.tv_num)
        var tvDate: TextView = v.findViewById(R.id.tv_date)
        var tVType: TextView = v.findViewById(R.id.tv_input_type)

        fun bind(model: OxygenBean) {
            tvNum.text = model.OOValue
            tvDate.text = DateUtil.instance.clipTimeFormatSecond(model.startTime)
            when (model.dataType) {
                "0" -> {
                    tVType.text = "手錶數值"
                }
                "1" -> {
                    tVType.text = "手動輸入數據"
                }
                else -> {
                    tVType.text = "醫事人員輸入數據"
                }
            }
        }
    }
}