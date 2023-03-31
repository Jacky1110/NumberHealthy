package com.jotangi.NumberHealthy.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jotangi.NumberHealthy.api.book.NotifyHistoryListVO
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.databinding.ItemNotifyHistoryBinding

// 已關懷
interface NotifyHistoryItemClickListener {
    fun onNotifyHistoryItemClick(vo: NotifyHistoryListVO)
}

class NotifyHistoryAdapter(
    private var data: List<NotifyHistoryListVO>,
    private val listener: NotifyHistoryItemClickListener?
) : RecyclerView.Adapter<NotifyHistoryAdapter.NotifyHistoryViewHolder>() {

    private val TAG = "${javaClass.simpleName}(TAG)"

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotifyHistoryAdapter.NotifyHistoryViewHolder {
        val binding = ItemNotifyHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotifyHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotifyHistoryViewHolder, position: Int) {
        holder.bind(
            data[position],
            listener
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun updateDataSource(dataSource: List<NotifyHistoryListVO>) {
        this.data = dataSource

        this.notifyDataSetChanged()
    }

    inner class NotifyHistoryViewHolder(val binding: ItemNotifyHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            model: NotifyHistoryListVO,
            mListener: NotifyHistoryItemClickListener?
        ) {
            binding.apply {
                notifyHistoryItemConstraintLayout.setOnClickListener {
                    mListener?.onNotifyHistoryItemClick(model)
                }
                when (model.status) {
                    "0" -> {
                        notifyHistoryItemImageView.setBackgroundResource(R.drawable.ic_baseline_mark_email_unread_24)
                    }
                    "1" -> {
                        notifyHistoryItemImageView.setBackgroundResource(R.drawable.ic_baseline_mark_email_read_24)
                    }
                }

                notifyHistoryItemTitleTextView.text = model.message_title
                notifyHistoryItemMessageFromTextView.text = model.message_from
                notifyHistoryItemContentTextView.text = model.message_descript
                notifyHistoryItemTimeTextView.text = model.message_date
            }
        }
    }
}