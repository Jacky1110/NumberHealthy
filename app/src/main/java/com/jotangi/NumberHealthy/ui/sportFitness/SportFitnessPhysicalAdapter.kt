package com.jotangi.NumberHealthy.ui.sportFitness

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.NumberHealthy.api.book.GetEvaluationLogListBeen
import com.jotangi.NumberHealthy.databinding.AdapterSportPhysicalBinding

class SportFitnessPhysicalAdapter(
    private val mData: List<GetEvaluationLogListBeen>
) : RecyclerView.Adapter<SportFitnessPhysicalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SportFitnessPhysicalAdapter.ViewHolder {
        return ViewHolder(
            AdapterSportPhysicalBinding
                .inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        )
    }

    override fun onBindViewHolder(
        holder: SportFitnessPhysicalAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(mData[position], position)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder(val binding: AdapterSportPhysicalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bean: GetEvaluationLogListBeen, position: Int) {
            binding.apply {
                with(bean) {
                    order.text = (position + 1).toString()
                    measurementTime.text = start_time?.substring(0, start_time!!.length - 9)
                    testItems.text = course
                    testResults.text = result_value
                    recommendedStandard.text = target
                }
            }
        }
    }
}