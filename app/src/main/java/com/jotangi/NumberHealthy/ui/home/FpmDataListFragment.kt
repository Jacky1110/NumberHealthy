package com.jotangi.NumberHealthy.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.NumberHealthy.api.ApiConstant
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.Module.GlobalVariable
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.databinding.FragmentFpmDataListBinding
import com.jotangi.NumberHealthy.utils.IoUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class FpmDataListFragment : BaseFragment() {

    private lateinit var binding: FragmentFpmDataListBinding
    override fun getToolBar() = binding.toolbar

    private lateinit var fpmAdapter: FpmAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFpmDataListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarArrow("足壓記錄")

        bookViewModel.fpmList.observe(viewLifecycleOwner) {

            val dateList = ArrayList<String>()
            for (i in 0 until it.length()) {
                dateList.add(it.getJSONObject(i).getString("mtime"))
            }

            fpmAdapter = FpmAdapter(dateList, object : FpmAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {

//                    loadItem(position)
                    GlobalVariable.setFpmPosition(position)
                    findNavController().navigate(R.id.fpmItemFragment)
                }

            })
            binding.rvFpm.apply {
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                adapter = fpmAdapter
            }
        }
    }

    private fun loadItem(index: Int) {

        showProgress()
        Log.e(TAG, "position: $index")

        val latestUrl = ApiConstant.ASIAFOOT_PDF_URL +
                bookViewModel.fpmList.value!!.getJSONObject(0).getString("pdf")

        val file = File(requireActivity().filesDir, "fpm_item.png")
        CoroutineScope(Dispatchers.IO).launch {
            val isLatest = IoUtils.pictureUrlToFile(latestUrl, file.path)
            Log.d(TAG, "讀取第 ${index + 1} 圖檔: $isLatest")

            closeProgress()

            CoroutineScope(Dispatchers.Main).launch {

                findNavController().navigate(R.id.fpmItemFragment)
            }
        }
    }
}