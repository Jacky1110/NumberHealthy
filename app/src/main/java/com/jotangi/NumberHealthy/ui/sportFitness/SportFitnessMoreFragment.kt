package com.jotangi.NumberHealthy.ui.sportFitness

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.NumberHealthy.api.book.GetActivityLogListBeen
import com.jotangi.NumberHealthy.api.book.GetEvaluationLogListBeen
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.databinding.FragmentSportFitnessMoreBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding

class SportFitnessMoreFragment : BaseFragment() {

    private lateinit var binding: FragmentSportFitnessMoreBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private val physicalList = arrayListOf<GetEvaluationLogListBeen>()

    private val documentalList = arrayListOf<GetActivityLogListBeen>()

    private lateinit var sportFitnessPhysicalAdapter: SportFitnessPhysicalAdapter

    private lateinit var sportFitnessDocumentalAdapter: SportFitnessDocumentalAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSportFitnessMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initCallBack()
    }


    private fun initView() {
        val value = arguments?.getString("value")
        setToolbarArrow("運動健身鏡")

        binding.apply {
            if (value == "PHYSICAL") {
                sportFitnessTitle.text = "體適能檢測紀錄"

                sportFitnessPhysicalAdapter = SportFitnessPhysicalAdapter(physicalList)
                fitnessMoreRecyclerview.apply {
                    layoutManager = LinearLayoutManager(context)
                    addItemDecoration(
                        DividerItemDecoration(
                            context,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                    adapter = sportFitnessPhysicalAdapter
                }
            } else if (value == "DOCUMENTAL") {
                sportFitnessTitle.text = "運動紀錄"

                sportFitnessDocumentalAdapter = SportFitnessDocumentalAdapter(documentalList)
                fitnessMoreRecyclerview.apply {
                    layoutManager = LinearLayoutManager(context)
                    addItemDecoration(
                        DividerItemDecoration(
                            context,
                            DividerItemDecoration.VERTICAL
                        )
                    )
                    adapter = sportFitnessDocumentalAdapter
                }
            }
        }

    }

    private fun initCallBack() {
        val value = arguments?.getString("value")
        if (value == "PHYSICAL") {
            bookViewModel.getEvaluationLogList.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    binding.apply {
                        physicalList.clear()
                        physicalList.addAll(it)
                        sportFitnessPhysicalAdapter.notifyDataSetChanged()
                    }
                }
            }
        } else if (value == "DOCUMENTAL") {
            bookViewModel.getActivityLogList.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    binding.apply {
                        documentalList.clear()
                        documentalList.addAll(it)
                        sportFitnessDocumentalAdapter.notifyDataSetChanged()
                    }
                }
            }

        }
    }
}