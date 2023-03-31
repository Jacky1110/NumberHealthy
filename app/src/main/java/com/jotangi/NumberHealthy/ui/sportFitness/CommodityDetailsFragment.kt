package com.jotangi.NumberHealthy.ui.sportFitness

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.databinding.FragmentCommodityDetailsBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.journeyapps.barcodescanner.BarcodeEncoder
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class CommodityDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentCommodityDetailsBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private val args by navArgs<CommodityDetailsFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCommodityDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        init()
        reciprocal()
    }


    private fun initView() {
        setToolbarArrow("商品兌換")

        Log.d("TAG", "bean: ${args.getCouponDataBeen}")
        binding.apply {
            commodityTitleTextview.text = args.getCouponDataBeen.coupon_description
            commodityNameTextview.text = "兌換商品:\n${args.getCouponDataBeen.coupon_name}"
            commodityCountTextview.text = "數量:\n${args.getCouponDataBeen.coupon_count}"
        }

    }

    private fun init() {
        var str: String? = null
        var jsonObject = JSONObject()
        try {
            jsonObject.put("customer_id",SharedPreferencesUtil.instances.getAccountId()?.enAes()?.trim())
            jsonObject.put("coupon_id",args.getCouponDataBeen.coupon_id?.enAes()?.trim())
            jsonObject.put("coupon_count",args.getCouponDataBeen.coupon_count?.enAes()?.trim())

            str = jsonObject.toString()
            Log.d("TAG", "str: ${str}")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val encoder = BarcodeEncoder()
        try {
            val bit = encoder.encodeBitmap(
                str, BarcodeFormat.QR_CODE,
                250, 250
            )
            binding.commodityImageview.setImageBitmap(bit)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

    private fun reciprocal() {

        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 30)
        calendar.set(Calendar.SECOND, 0)

        val sdf = SimpleDateFormat(
            "mm:ss", Locale.getDefault()
        )

        object : CountDownTimer(1800000, 1000) {
            override fun onTick(p0: Long) {
                calendar.add(Calendar.SECOND, -1)
                binding.commodityTimeTextview.text = sdf.format(calendar.time)
            }

            override fun onFinish() {
                binding.commodityImageview.visibility = View.INVISIBLE
            }

        }.start()
    }

}
