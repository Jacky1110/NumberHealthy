package com.jotangi.NumberHealthy.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.NumberHealthy.R

class FpmAdapter(private val mData: List<String>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<FpmAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FpmAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_fpm, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val dateTV: TextView = v.findViewById(R.id.tv_item_fpm_date)
        val item: ConstraintLayout = v.findViewById(R.id.cl_item)

        fun bind(date: String) {
            dateTV.text = date
            item.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}