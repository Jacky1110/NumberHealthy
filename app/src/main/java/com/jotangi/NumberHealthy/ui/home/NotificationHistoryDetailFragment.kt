package com.jotangi.NumberHealthy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.gson.Gson
import com.jotangi.NumberHealthy.api.book.BookApiRepository
import com.jotangi.NumberHealthy.api.book.NotifyHistoryListVO
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.databinding.FragmentNotificationHistoryDetailBinding
import com.jotangi.NumberHealthy.utils.DialogUtils
import kotlinx.coroutines.launch

class NotificationHistoryDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentNotificationHistoryDetailBinding
    override fun getToolBar() = binding.toolbar

    val args: NotificationHistoryDetailFragmentArgs by navArgs()
    private lateinit var data: NotifyHistoryListVO

    val apiRepository: BookApiRepository by lazy { BookApiRepository() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        data = Gson().fromJson(
            args.notificationHistoryVo,
            NotifyHistoryListVO::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationHistoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initView()
        initAction()
    }

    private fun initData() {
        if (data.status == "0" && data.message_type == "0") {
            updateNotifyHistoryReadStatus()
        }
    }

    private fun updateNotifyHistoryReadStatus() {
        lifecycleScope.launch {
            bookViewModel.updateMessageBoxReadStatus(data.rid!!)
        }
    }

    private fun initView() {
        setToolbarArrow("訊息")
        updateView()
    }

    private fun updateView() {
        binding.apply {
            notifyHistoryDetailTitleTextView.text = data.message_title
            notifyHistoryDetailContentTextView.text = data.message_descript

            when (data.message_type) {
                "0" -> {
                    agreeAuthMaterialButton.visibility = View.GONE
                    disagreeAuthMaterialButton.visibility = View.GONE
                }

                "1" -> {
                    if (data.status == "0") {
                        agreeAuthMaterialButton.visibility = View.VISIBLE
                        disagreeAuthMaterialButton.visibility = View.VISIBLE
                    } else {
                        agreeAuthMaterialButton.visibility = View.GONE
                        disagreeAuthMaterialButton.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun initAction() {

        binding.apply {

            agreeAuthMaterialButton.setOnClickListener {

                if (data.message_from?.isNotEmpty() == true) {

                    showProgress()

                    updateNotifyHistoryReadStatus()

                    lifecycleScope.launch {

                        val response = apiRepository.authCareStatus(
                            "1",
                            data.message_from!!
                        )

                        closeProgress()

                        when (response.code) {

                            "0x0200" -> findNavController().navigate(R.id.navigation_home)
                            "0x0207" -> {

                                DialogUtils.showSingle(
                                    requireActivity(),
                                    "",
                                    response.responseMessage,
                                ) {
                                    findNavController().navigate(R.id.navigation_home)
                                }
                            }
                        }
                    }
                }
            }

            disagreeAuthMaterialButton.setOnClickListener {

                if (data.message_from?.isNotEmpty() == true) {

                    showProgress()

                    updateNotifyHistoryReadStatus()

                    lifecycleScope.launch {

                        val response = apiRepository.authCareStatus(
                            "2",
                            data.message_from!!
                        )

                        closeProgress()

                        when (response.code) {

                            "0x0200" -> findNavController().navigate(R.id.navigation_home)
                        }
                    }
                }
            }
        }
    }
}