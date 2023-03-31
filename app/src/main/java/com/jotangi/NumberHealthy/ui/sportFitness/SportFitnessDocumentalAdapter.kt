package com.jotangi.NumberHealthy.ui.sportFitness

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.NumberHealthy.api.book.GetActivityLogListBeen
import com.jotangi.NumberHealthy.databinding.AdapterSportDocumentalBinding

class SportFitnessDocumentalAdapter(
    private val mData: List<GetActivityLogListBeen>
) : RecyclerView.Adapter<SportFitnessDocumentalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SportFitnessDocumentalAdapter.ViewHolder {
        return ViewHolder(
            AdapterSportDocumentalBinding
                .inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
        )
    }

    override fun onBindViewHolder(
        holder: SportFitnessDocumentalAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(mData[position], position)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder(val binding: AdapterSportDocumentalBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bean: GetActivityLogListBeen, position: Int) {
            binding.apply {
                with(bean) {
                    order.text = (position + 1).toString()
                    measurementTime.text = start_time?.substring(0, start_time!!.length - 9)
                    testItems.text = course
                    testResults.text = elapsed_time
                    recommendedStandard.text = cal
                }
            }
        }
    }
}