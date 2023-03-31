package com.jotangi.NumberHealthy.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.databinding.DialogCustomBinding
import com.jotangi.NumberHealthy.databinding.DialogMultiMessageBinding
import com.jotangi.NumberHealthy.databinding.ProgressLoadingBinding

class DialogUtil private constructor() {

    private val TAG = "DialogUtil"

    companion object {
        val instance: DialogUtil = DialogUtil()
    }

    /**
     * Progress
     */
    fun loadingShow(activity: Activity, pd: (dialog: Dialog) -> Unit) {

        val viewGroup = activity.findViewById<ViewGroup>(android.R.id.content)
        val binding = ProgressLoadingBinding.inflate(
            LayoutInflater.from(activity), viewGroup, false
        )
        val dialog = Dialog(activity, R.style.NewDialog)
        dialog.setCancelable(false)
        dialog.setContentView(binding.root)
        dialog.show()
        pd(dialog)
    }

    /**
     * 單選
     */
    fun singleMessageDialog(
        activity: Activity,
        title: String,
        content: String,
        cancelClick: () -> Unit
    ) {
        singleMessageDialog(activity, title, content, "關閉", cancelClick)
    }

    fun singleMessageDialog(
        activity: Activity,
        title: String,
        content: String,
        cancel: String,
        cancelClick: () -> Unit
    ) {
        val viewGroup = activity.findViewById<ViewGroup>(android.R.id.content)
        val binding = DialogCustomBinding.inflate(
            LayoutInflater.from(activity), viewGroup, false
        )

        val alertDialog = AlertDialog.Builder(activity)
            .setView(binding.root)
            .setCancelable(false)
            .create()

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.apply {

            if (title.isEmpty()) {
                tvDialogTitle.visibility = View.GONE
            } else {
                tvDialogTitle.text = title
            }
            tvDialogContent.text = content
            tvDialogCloseContent.text = cancel

            tvDialogCloseContent.setOnClickListener {
                alertDialog.dismiss()
                cancelClick.invoke()
            }
        }

        alertDialog.show()
    }

    /**
     * 多選
     */
    fun multiMessageDialog(
        activity: Activity,
        title: String,
        content: String,
        okTxt: String,
        cancelTxt: String,
        okClick: () -> Unit
    ) {
        val vg = activity.findViewById<ViewGroup>(android.R.id.content)
        val binding = DialogMultiMessageBinding.inflate(
            LayoutInflater.from(activity), vg, false
        )
        val ad = AlertDialog.Builder(activity)
            .setCancelable(false)
            .setView(binding.root)
            .create()

        binding.apply {

            if (title.isEmpty()) {
                tvTitle.visibility = View.GONE
            } else {
                tvTitle.text = title
            }

            tvContent.text = content

            if (okTxt.isNotBlank()) {
                tvOk.text = okTxt
            }

            if (cancelTxt.isNotBlank()) {
                tvCancel.text = cancelTxt
            }

            tvOk.setOnClickListener {
                ad.dismiss()
                okClick()
            }

            tvCancel.setOnClickListener {
                ad.dismiss()
            }
        }

        ad.window?.setBackgroundDrawableResource(android.R.color.transparent)
        ad.show()
    }
}