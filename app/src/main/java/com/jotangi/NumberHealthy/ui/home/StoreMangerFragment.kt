package com.jotangi.NumberHealthy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.jotangi.NumberHealthy.api.book.BookApiRepository
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.databinding.FragmentStoreMangerBinding
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.DialogUtils
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.jotangi.NumberHealthy.utils.smartwatch.WatchUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


class StoreMangerFragment : BaseFragment() {

    private lateinit var binding: FragmentStoreMangerBinding
    override fun getToolBar() = binding.toolbar

    private val apiRepository: BookApiRepository by lazy { BookApiRepository() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStoreMangerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initCallBack()
    }

    private fun initView() {
        setOutToolbarArrow("數體健店長")
        storeLogout.visibility = View.VISIBLE
        activity?.nav_view?.visibility = View.INVISIBLE
        val startTime = DateUtil.instance.ago3MonthYmdHms()
        val endTime = DateUtil.instance.currentYmdHms()

        binding.apply {
            mangerQrImageview.setOnClickListener {
                findNavController().navigate(R.id.scanCouponFragment)
            }

            documentalImageview.setOnClickListener {

                arguments?.clear()

                if (SharedPreferencesUtil.instances.getAccountId().isNullOrBlank()) {
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    showProgress()

                    bookViewModel.getHistoryList(startTime, endTime)
                    closeProgress()
                    findNavController().navigate(R.id.historicalRecordFragment)

                }
            }
        }
        storeLogout.setOnClickListener {
            lifecycleScope.launch {
                showProgress()
                val response = apiRepository.userLogout()
                closeProgress()
                if (!response.code.isNullOrBlank()) {
                    SharedPreferencesUtil.instances.setAccountId(null)
                    SharedPreferencesUtil.instances.setAccountPwd(null)
                    SharedPreferencesUtil.instances.setAccountMid(null)
                    bookViewModel.clearData()
                    FirebaseMessaging.getInstance().deleteToken()
                    CoroutineScope(Dispatchers.Main).launch {
                        storeLogout.visibility = View.INVISIBLE
                        activity?.nav_view?.visibility = View.VISIBLE
                        findNavController().navigate(
                            StoreMangerFragmentDirections.actionStoreMangerFragmentToLoginFragment()
                        )
                    }
                }
            }
        }
    }

    private fun initCallBack() {
        val jsonValue = arguments?.getString("jsonValue", "")
        if (!jsonValue.isNullOrBlank()) {
            try {
                val jsonObject = JSONObject(jsonValue)
                val id = jsonObject.getString("customer_id").deAes()
                val coupon = jsonObject.getString("coupon_id").deAes()
                val count = jsonObject.getString("coupon_count").deAes()
                showProgress()
                lifecycleScope.launch {
                    val response = apiRepository.getScanCoupon(id, coupon, count)
                    closeProgress()
                    if (response.code != "0x0200") {

                        showErrorMsgDialog(response.responseMessage.toString())

                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            DialogUtils.showSingle(
                                requireActivity(),
                                "訊息",
                                "兌換成功"
                            ) {

                            }
                        }
                    }

                }

            } catch (e: Exception) {
                e.printStackTrace()
                showErrorMsgDialog("格式錯誤")

            }
        }
    }
}