package com.jotangi.NumberHealthy.ui.Member

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.databinding.FragmentMemberQrBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.journeyapps.barcodescanner.BarcodeEncoder

class MemberQrFragment : BaseFragment() {

    private lateinit var binding: FragmentMemberQrBinding

    override fun getToolBar(): ToolbarBinding = binding.toolbar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMemberQrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        setToolbarArrow("會員QR Code")

        var str: String? = null
        try {

            str = SharedPreferencesUtil.instances.getAccountId()?.enAes()?.trim()
            Log.d(TAG, "str: ${str}")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val encoder = BarcodeEncoder()
        try {
            val bit = encoder.encodeBitmap(
                str, BarcodeFormat.QR_CODE,
                250, 250
            )
            binding.memberQRImageview.setImageBitmap(bit)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
}