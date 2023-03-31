package com.jotangi.NumberHealthy.ui.Member

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.NumberHealthy.databinding.ItemWatchBinding
import com.yucheng.ycbtsdk.bean.ScanDeviceBean

class WatchAdapter(private val deviceList: List<ScanDeviceBean>) :
    RecyclerView.Adapter<WatchAdapter.ViewHolder>() {

    var watchItemClick: (ScanDeviceBean) -> Unit = {}

    inner class ViewHolder(private val binding: ItemWatchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ScanDeviceBean) {

            binding.apply {

                waId.text = item.deviceName
                waDe.text = item.deviceMac
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemWatchBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(deviceList[position])

        holder.itemView.setOnClickListener {
            watchItemClick.invoke(deviceList[position])
        }
    }

    override fun getItemCount(): Int {
        return deviceList.size
    }
}
