package com.jotangi.NumberHealthy.ui.Member

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.NumberHealthy.api.book.CareListVO
import com.jotangi.NumberHealthy.databinding.ItemCaredBinding

// 關注我的
interface CaredCancelClickListener {
    fun onCaredCancelClick(vo: CareListVO)
}

class CaredAdapter(
    private var data: List<CareListVO>,
    private val cancelListener: CaredCancelClickListener?
) : RecyclerView.Adapter<CaredAdapter.CaredViewHolder>() {

    private val TAG = "${javaClass.simpleName}(TAG)"

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CaredAdapter.CaredViewHolder {
        val binding = ItemCaredBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CaredViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CaredViewHolder, position: Int) {
        holder.bind(
            data[position],
            cancelListener
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateDataSource(dataSource: List<CareListVO>) {
        this.data = dataSource

        this.notifyDataSetChanged()
    }

    inner class CaredViewHolder(val binding: ItemCaredBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            model: CareListVO,
            mCancelListener: CaredCancelClickListener?
        ) {
            binding.apply {
                tvCaredAccount.text = model.fmember_id
                tvCartedStatus.text = model.fmember_name
                tvCaredDel.text = "取消授權"
                tvCaredEditName.visibility = View.GONE
                tvCaredDel.setOnClickListener {
                    mCancelListener?.onCaredCancelClick(model)
                }
            }

        }
    }
}