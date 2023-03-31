package com.jotangi.NumberHealthy.ui.home

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jotangi.NumberHealthy.api.ApiConstant
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.databinding.FragmentFpmBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.squareup.picasso.Picasso
import java.io.File

class FpmFragment : BaseFragment() {

    private lateinit var binding: FragmentFpmBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFpmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarDetail(
            getString(R.string.circle_foot_pressure_measurement),
            R.id.fpmDataListFragment
        )

        bookViewModel.fpmList.observe(viewLifecycleOwner) {

            binding.apply {

                if (it.length() > 0) {

                    ivFpmFootsize.setImageBitmap(
                        BitmapFactory.decodeFile(
                            File(requireActivity().filesDir, "fpm.png").path
                        )
                    )
                    nsvIv.visibility = View.VISIBLE
                    Log.d(TAG, "========= 有圖 =========")

                    bookViewModel.fpmList.observe(viewLifecycleOwner) {
                        val pdf = it.getJSONObject(0).getString("pdf")
                        when (it.getJSONObject(0).getString("type")) {
                            "0" -> {
                                Picasso.get()
                                    .load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_P + pdf)
                                    .into(ivFpmTwo)
                                Picasso.get()
                                    .load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_DE + pdf)
                                    .into(ivFpmThree)
                            }
                            "1" -> {
                                Picasso.get()
                                    .load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_KI + pdf)
                                    .into(ivFpmTwo)
                                Picasso.get()
                                    .load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_KP + pdf)
                                    .into(ivFpmThree)
                            }
                            "2" -> {
                                Picasso.get()
                                    .load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_WP + pdf)
                                    .into(ivFpmTwo)
                                Picasso.get()
                                    .load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_DE + pdf)
                                    .into(ivFpmThree)
                                Picasso.get()
                                    .load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_TINEA + pdf)
                                    .into(ivFpmFour)
                            }
                            "3" -> {
                                Picasso.get()
                                    .load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_P + pdf)
                                    .into(ivFpmTwo)
                                Picasso.get()
                                    .load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_DE + pdf)
                                    .into(ivFpmThree)
                                Picasso.get()
                                    .load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_TINEA + pdf)
                                    .into(ivFpmFour)
                            }
                        }
                    }

                } else {

                    clFpmNoData.visibility = View.VISIBLE
                    Log.d(TAG, "========= 無圖 =========")
                }
            }
        }
    }
}