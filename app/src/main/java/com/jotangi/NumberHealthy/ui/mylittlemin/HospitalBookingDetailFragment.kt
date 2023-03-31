package com.jotangi.NumberHealthy.ui.mylittlemin

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jotangi.NumberHealthy.api.ApiConstant
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.databinding.FragmentHospitalBookingDetailBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

// 有拍照功能
class HospitalBookingDetailFragment : BaseFragment() {
    private val args by navArgs<HospitalBookingDetailFragmentArgs>()

    private var _binding: FragmentHospitalBookingDetailBinding? = null
    private val binding get() = _binding!!
    override fun getToolBar(): ToolbarBinding? = binding.toolbar


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHospitalBookingDetailBinding.inflate(inflater, container, false)
        binding.apply {
            bdr.layoutManager = LinearLayoutManager(context)
            bdr.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolBookDetail()


        bookViewModel.bookingInfo2LiveData.observe(viewLifecycleOwner) { it ->
            binding.apply {
                bd1.text = args.title
                bd2.text = it.member_name
                bd3.text = it.member_phone
                if (it.reserve_status == "0") {
                    bdcel.isEnabled = true
                    bdcel.text = "取消預約"
                    l1.visibility = View.VISIBLE
                } else if (it.reserve_status == "1") {
                    bdcel.isEnabled = true
                    bdcel.text = "取消預約"
                    l1.visibility = View.VISIBLE
                } else if (it.reserve_status == "2") {
                    bdcel.isEnabled = false
                    bdcel.text = "預約已取消"
                    l1.visibility = View.INVISIBLE

                } else {
                    bdcel.isEnabled = false
                    bdcel.text = "遠距門診已結束"
                    l1.visibility = View.INVISIBLE

                }

                bdt2.setText(it.message_log)
                bdt2.isEnabled = false

                Glide.with(root).load(ApiConstant.IMAGE_URL + it.message_pic).into(bdimg)

            }
        }

        lifecycleScope.launch {

            if (RegisterIdCameraFragment.bmp != null) {
                binding.bdimg.setImageBitmap(RegisterIdCameraFragment.bmp)
            }
            RegisterIdCameraFragment.bmp?.let {
                bitmapToFile(
                    it
                )
            }?.let {
                bookViewModel.upload_messagepic(
                    args.bookingNo, it
                )
                RegisterIdCameraFragment.bmp = null
            }
            bookViewModel.getBookingInfo2(args.bookingNo)
        }

        binding.bdcel.apply {
            setOnClickListener {
                lifecycleScope.launch {
                    bookViewModel.cancelBooking2(args.bookingNo)
                }
                isEnabled = false
                text = "已取消"
            }
        }
        binding.bdC.setOnClickListener {
            findNavController().navigate(R.id.registerIdCameraFragment)
        }
        binding.bdsend.setOnClickListener {
            lifecycleScope.launch {
                when {
                    binding.bdin.text.toString().isNullOrEmpty() -> {
                        Toast.makeText(context, "請輸入訊息喔", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        bookViewModel.upload_messagelog(
                            args.bookingNo,
                            binding.bdin.text.toString()
                        )
                        binding.bdin.setText("")
                        bookViewModel.getBookingInfo2(args.bookingNo)
                    }
                }
            }
        }

    }

    private fun bitmapToFile(bitmap: Bitmap): File {//獲取imageview 的bitmap 轉成檔案格式
        val wrapper = ContextWrapper(context)
        var file: File? = null

        // Initialize a new file instance to save bitmap object
        file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file.createNewFile()
        file = File(file, "${UUID.randomUUID()}.jpg")

        try {
            // Compress the bitmap and save in jpg format
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
        } finally {
            file
        }
        return file
    }
}