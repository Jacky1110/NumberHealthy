package com.jotangi.NumberHealthy.ui.home.watch

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.api.watch.EcgBean
import com.jotangi.NumberHealthy.utils.DateUtil
import com.yucheng.ycbtsdk.Constants

class EcgAdapter(private val mData: List<EcgBean>) : RecyclerView.Adapter<EcgAdapter.ViewHolder>() {

    private val TAG = "${javaClass.simpleName}(TAG)"

    var ecgItemClick: (EcgBean) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EcgAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_ecg, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EcgAdapter.ViewHolder, position: Int) {
        holder.bind(mData[position])

        val data = mData[position]
        holder.itemView.setOnClickListener {
            ecgItemClick.invoke(data)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var heart: TextView = v.findViewById(R.id.tv_ecg_heart)
        var bp: TextView = v.findViewById(R.id.tv_ecg_bp)
        var hrv: TextView = v.findViewById(R.id.tv_ecg_hrv)
        var date: TextView = v.findViewById(R.id.tv_ecg_date)

        var llLeft: LinearLayout = v.findViewById(R.id.ll_item_ecg_left)
        var tvLeft: TextView = v.findViewById(R.id.tv_item_ecg_sync)

        fun bind(model: EcgBean) {
            date.text = DateUtil.instance.clipTimeFormatSecond(model.ecgStartTime)

            if (model.hr == Constants.DATATYPE.Real_UploadECG) {
                Log.d(TAG, "model.hr: ${model.hr}")
                Log.d(TAG, "model.ecgStartTime: ${model.ecgStartTime}")

                llLeft.visibility = View.INVISIBLE
                tvLeft.visibility = View.VISIBLE
            } else {
                llLeft.visibility = View.VISIBLE
                tvLeft.visibility = View.INVISIBLE

                heart.text = model.hr.toString()
                val sbp = if (model.sbp == 0) "--" else model.sbp.toString()
                val dbp = if (model.dbp == 0) "--" else model.dbp.toString()
                bp.text = "$sbp/$dbp"
                hrv.text = model.hrv.toString()
            }
        }
    }

}