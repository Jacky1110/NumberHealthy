package com.jotangi.NumberHealthy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.jotangi.NumberHealthy.api.book.NotifyHistoryListVO
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.databinding.FragmentNotificationHistoryBinding
import kotlinx.coroutines.launch

class NotificationHistoryFragment :
    BaseFragment(),
    NotifyHistoryItemClickListener {

    private lateinit var binding: FragmentNotificationHistoryBinding
    override fun getToolBar() = binding.toolbar

    private lateinit var notifyHistoryAdapter: NotifyHistoryAdapter
    private var msgData = listOf<NotifyHistoryListVO>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initView()
    }

    private fun initObserver() {
        bookViewModel.msgBoxList.observe(viewLifecycleOwner) { result ->
            if (result != null && result.isNotEmpty()) {
                msgData = result
                notifyHistoryAdapter.updateDataSource(msgData)
            }
        }
    }

    private fun initData() {
        lifecycleScope.launch {
            bookViewModel.getMsgBoxList()
        }
    }

    private fun initView() {
        setToolbarArrow("訊息")

        binding.apply {
            notifyHistoryRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                notifyHistoryAdapter = NotifyHistoryAdapter(
                    msgData,
                    this@NotificationHistoryFragment,
                )
                this.adapter = notifyHistoryAdapter
            }
        }
    }

    override fun onResume() {
        super.onResume()

        initData()
    }

    override fun onNotifyHistoryItemClick(vo: NotifyHistoryListVO) {
        val notifyJsonString = Gson().toJson(vo)

//        findNavController().navigate(R.id.notification_history_fragment)
        val action = NotificationHistoryFragmentDirections.toNotifyDetail(notifyJsonString)
        findNavController().navigate(action)
    }
}