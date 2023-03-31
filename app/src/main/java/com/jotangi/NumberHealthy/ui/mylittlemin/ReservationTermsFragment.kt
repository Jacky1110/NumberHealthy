package com.jotangi.NumberHealthy.ui.mylittlemin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.databinding.FragmentReservationTermsBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding

// 拍照
class ReservationTermsFragment : BaseFragment() {

    private var _binding: FragmentReservationTermsBinding? = null
    private val binding get() = _binding!!
    override fun getToolBar(): ToolbarBinding? = binding.toolbar

    private val args by navArgs<ReservationTermsFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReservationTermsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolBookStep0()

        val sid = args.sid
        val storename = args.storeName
        Log.d(TAG, "sid: $sid storename: $storename")

        bookViewModel.isoK.observe(viewLifecycleOwner) {

            if (it) {
                binding.btOpen.text = "已上傳，下一步"
                binding.imageButton.visibility = View.INVISIBLE
            } else {
                binding.btOpen.text = "照片拍攝"
            }
        }

        binding.apply {
            btOpen.setOnClickListener {
                bookViewModel.isoK.observe(viewLifecycleOwner) {

                    if (it) {

//                        findNavController().navigate(
//                            ReservationTermsFragmentDirections.toHB2(args.sid!!, args.storeName!!)
//                        )

                    } else {

                        findNavController().navigate(
                            ReservationTermsFragmentDirections.tocamera(
                                args.sid!!,
                                args.storeName!!
                            )
                        )
                    }

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}