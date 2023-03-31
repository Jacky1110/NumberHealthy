package com.jotangi.NumberHealthy.ui.home.watch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.api.watch.GetStepsBean
import com.jotangi.NumberHealthy.utils.DateUtil

class StepAdapter(private val mData: List<GetStepsBean>) :
    RecyclerView.Adapter<StepAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_footstep, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvFootStepNum: TextView = v.findViewById(R.id.tv_footstep_num)
        var tvFootStepDate: TextView = v.findViewById(R.id.tv_footstep_date)

        fun bind(model: GetStepsBean) {
            tvFootStepNum.text = model.sportStep.toString()
            tvFootStepDate.text = DateUtil.instance.clipTimeToYMD(model.sportStartTime)
        }
    }
}