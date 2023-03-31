package com.jotangi.NumberHealthy.ui.sportFitness

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.api.book.GetCouponDataBeen
import com.jotangi.NumberHealthy.databinding.FragmentFitnessMirrorCouponListBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.utils.DialogDateUtil
import com.jotangi.NumberHealthy.utils.DialogUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FitnessMirrorCouponListFragment : BaseFragment() {

    private lateinit var binding: FragmentFitnessMirrorCouponListBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private val couponList = arrayListOf<GetCouponDataBeen>()
    private lateinit var fitnessMirrorCouponListAdapter: FitnessMirrorCouponListAdapter

    private var memberPoint: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFitnessMirrorCouponListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initCallBack()
    }

    private fun initView() {

        setToolbarArrow("運動健身鏡")

        fitnessMirrorCouponListAdapter = FitnessMirrorCouponListAdapter(couponList)
        binding.fitnessMirrorCouponListRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = fitnessMirrorCouponListAdapter

            fitnessMirrorCouponListAdapter.watchItemClick = { bean ->
                DialogDateUtil.instance.convert(requireActivity(), numValue = {
                    bean.coupon_count = it
                    Log.d(TAG, "memberPoint: ${memberPoint}")
                    if (bean.coupon_count != "0" && !memberPoint.isNullOrBlank() && memberPoint != "0") {
                        findNavController().navigate(
                            FitnessMirrorCouponListFragmentDirections.actionFitnessMirrorCouponListFragmentToCommodityDetailsFragment(
                                bean
                            )
                        )
                    } else if (bean.coupon_count == "0") {
                        CoroutineScope(Dispatchers.Main).launch {
                            DialogUtils.showSingle(
                                requireActivity(),
                                "訊息",
                                "商品數量不可為0"
                            ) {

                            }
                        }
                    }
                    if (memberPoint.isNullOrBlank() || memberPoint!! < bean.coupon_count!! || memberPoint == "0") {
                        CoroutineScope(Dispatchers.Main).launch {
                            DialogUtils.showSingle(
                                requireActivity(),
                                "訊息",
                                "點數不足"
                            ) {

                            }
                        }
                    }

                    Log.d(TAG, "numValue: $it")
                })
            }
        }
    }

    private fun initCallBack() {
        bookViewModel.getCouponList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.apply {
                    couponList.clear()
                    couponList.addAll(it)
                    fitnessMirrorCouponListAdapter.notifyDataSetChanged()
                }
            }
        }

        bookViewModel.getCouponPoint.observe(viewLifecycleOwner) {
            it?.let { point ->
                binding.apply {
                    binding.apply {
                        val value = arguments?.getString("value")
                        if (value == "true") {
                            fitnessMirrorPointTextview.text = "您現有 ${point} 點"
                        } else {
                            fitnessMirrorPointTextview.text = "您現有 0點"
                        }
                    }
                }
                memberPoint = point

            }


        }
    }
}