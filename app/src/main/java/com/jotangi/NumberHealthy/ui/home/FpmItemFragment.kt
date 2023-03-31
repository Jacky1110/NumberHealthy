package com.jotangi.NumberHealthy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jotangi.NumberHealthy.api.ApiConstant
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.Module.GlobalVariable
import com.jotangi.NumberHealthy.databinding.FragmentFpmItemBinding
import com.squareup.picasso.Picasso

class FpmItemFragment : BaseFragment() {

    private lateinit var binding: FragmentFpmItemBinding
    override fun getToolBar() = binding.toolbar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFpmItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarArrow("")

        bookViewModel.fpmList.observe(viewLifecycleOwner) {

            val pdf = it.getJSONObject(GlobalVariable.getFpmPosition()).getString("pdf")

            binding.apply {

                Picasso.get().load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.MAIN_FOOTSIZE + pdf)
                    .into(ivFpmFootsize)

                when (it.getJSONObject(GlobalVariable.getFpmPosition()).getString("type")) {
                    "0" -> {
                        Picasso.get().load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_P + pdf)
                            .into(ivFpmTwo)
                        Picasso.get().load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_DE + pdf)
                            .into(ivFpmThree)
                    }
                    "1" -> {
                        Picasso.get().load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_KI + pdf)
                            .into(ivFpmTwo)
                        Picasso.get().load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_KP + pdf)
                            .into(ivFpmThree)
                    }
                    "2" -> {
                        Picasso.get().load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_WP + pdf)
                            .into(ivFpmTwo)
                        Picasso.get().load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_DE + pdf)
                            .into(ivFpmThree)
                        Picasso.get()
                            .load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_TINEA + pdf)
                            .into(ivFpmFour)
                    }
                    "3" -> {
                        Picasso.get().load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_P + pdf)
                            .into(ivFpmTwo)
                        Picasso.get().load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_DE + pdf)
                            .into(ivFpmThree)
                        Picasso.get()
                            .load(ApiConstant.ASIAFOOT_PDF_URL + ApiConstant.ITEM_TINEA + pdf)
                            .into(ivFpmFour)
                    }
                }

            }

        }

//        binding.ivFpmFootsize.setImageBitmap(
//            BitmapFactory.decodeFile(
//                File(requireActivity().filesDir, "fpm_item.png").path
//            )
//        )
    }
}