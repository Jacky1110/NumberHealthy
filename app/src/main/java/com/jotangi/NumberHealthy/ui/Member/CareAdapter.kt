package com.jotangi.NumberHealthy.ui.Member

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.NumberHealthy.api.book.CareListVO
import com.jotangi.NumberHealthy.databinding.ItemCaredBinding

// 我關注的
interface CareEditClickListener {
    fun onCareEditClick(vo: CareListVO)
}

interface CareDeleteClickListener {
    fun onCareDeleteClick(vo: CareListVO)
}

class CareAdapter(
    private var data: List<CareListVO>,
    private val editListener: CareEditClickListener?,
    private val deleteListener: CareDeleteClickListener?
) : RecyclerView.Adapter<CareAdapter.CareViewHolder>() {

    private val TAG = "(TAG)${javaClass.simpleName}"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CareAdapter.CareViewHolder {
        val binding = ItemCaredBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CareViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CareViewHolder, position: Int) {
        holder.bind(
            data[position],
            editListener,
            deleteListener
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateDataSource(dataSource: List<CareListVO>) {
        this.data = dataSource

        this.notifyDataSetChanged()
    }

    inner class CareViewHolder(val binding: ItemCaredBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            model: CareListVO,
            mEditListener: CareEditClickListener?,
            mDeleteListener: CareDeleteClickListener?
        ) {
            binding.apply {
                tvCaredAccount.text = model.cmember_id
                when (model.status) {
                    "0" -> {
                        tvCartedStatus.text = "授權中"
                        tvCaredEditName.visibility = View.GONE
                    }

                    "1" -> {
                        if (model.nick_name.isNullOrBlank()) {
                            tvCartedStatus.text = ""
                        } else {
                            tvCartedStatus.text = model.nick_name
                        }

                        tvCaredEditName.visibility = View.VISIBLE
                    }

                    "2" -> {
                        tvCartedStatus.text = "不同意授權"
                        tvCaredEditName.visibility = View.GONE
                    }
                }

                tvCaredEditName.setOnClickListener {
                    mEditListener?.onCareEditClick(model)
                }

                tvCaredDel.setOnClickListener {
                    mDeleteListener?.onCareDeleteClick(model)
                }
            }
        }
    }
}