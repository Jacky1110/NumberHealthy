package com.jotangi.NumberHealthy.ui.mylittlemin

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.databinding.CameraResultBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding


class ImageResultFragment : BaseFragment() {

    private var _binding: CameraResultBinding? = null
    private val binding get() = _binding!!
    override fun getToolBar(): ToolbarBinding? = binding.toolbar

    companion object {
        lateinit var bmp: Bitmap
    }

    private val args by navArgs<ImageResultFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CameraResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setupToolbarBook()

        binding.reIM.setImageBitmap(CameraAllViewFragment.bmp)
        binding.reIM.isDrawingCacheEnabled = true
        binding.reIM.buildDrawingCache(true)
        val bitmap1 = binding.reIM.drawingCache
        binding.reIM.invalidate()
        val bitmap = binding.reIM.getDrawable().toBitmap()
        bmp = CameraAllViewFragment.bmp!!
        //  Glide.with(requireActivity()).load(CameraAllViewFragment.bmp).override(960,480).into(  binding.reIM)

        binding.reP.setOnClickListener {
            bookViewModel.setnotok()
            requireActivity().onBackPressed()
        }
        binding.reU.setOnClickListener {
            bookViewModel.setok()
            findNavController().navigate(
                ImageResultFragmentDirections.toRes(
                    args.sid,
                    args.storeName
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}