package com.jotangi.NumberHealthy.StoreMangerUi.googlevision

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.NumberHealthy.R

class StoreListAdapter(var data: List<StationListResponse>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_sm, parent, false)
        return BookRecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BookRecordViewHolder)
            holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class BookRecordViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var item: ConstraintLayout = v.findViewById(R.id.smL)
        var t1: TextView = v.findViewById(R.id.SMt1)
        var t2: TextView = v.findViewById(R.id.SMt2)
        var t3: TextView = v.findViewById(R.id.SMt3)
        var t4: TextView = v.findViewById(R.id.SMt4)
        fun bind(model: StationListResponse) {
            t1.text = model.service_name
            t2.text = model.member_name
            t3.text = model.reserve_created_at
            t4.text = model.member_id

        }
    }
}