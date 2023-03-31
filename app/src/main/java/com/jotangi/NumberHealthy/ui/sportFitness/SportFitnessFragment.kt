package com.jotangi.NumberHealthy.ui.sportFitness

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.NumberHealthy.api.book.GetActivityLogListBeen
import com.jotangi.NumberHealthy.api.book.GetEvaluationLogListBeen
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.databinding.FragmentSportFitnessBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding

class SportFitnessFragment : BaseFragment() {

    private lateinit var binding: FragmentSportFitnessBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private val physicalList = arrayListOf<GetEvaluationLogListBeen>()

    private val documentalList = arrayListOf<GetActivityLogListBeen>()

    private lateinit var sportFitnessPhysicalAdapter: SportFitnessPhysicalAdapter

    private lateinit var sportFitnessDocumentalAdapter: SportFitnessDocumentalAdapter

    private var memberPoint: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSportFitnessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initCallBack()
    }

    private fun initView() {
        setToolbarArrow("運動健身鏡")

        binding.apply {
            sportFitnessExchangeTextview.setOnClickListener {
                if (!memberPoint.isNullOrBlank()) {
                    val bundle = bundleOf("value" to "true")
                    findNavController().navigate(R.id.fitnessMirrorCouponListFragment, bundle)
                } else {
                    val bundle = bundleOf("value" to "false")
                    findNavController().navigate(R.id.fitnessMirrorCouponListFragment, bundle)
                }

            }

            physicalMore.setOnClickListener {
                val bundle = bundleOf("value" to "PHYSICAL")
                findNavController().navigate(R.id.sportFitnessMoreFragment, bundle)
            }

            documentalMore.setOnClickListener {
                val bundle = bundleOf("value" to "DOCUMENTAL")
                findNavController().navigate(R.id.sportFitnessMoreFragment, bundle)
            }

        }

        sportFitnessPhysicalAdapter = SportFitnessPhysicalAdapter(physicalList)
        binding.physicalRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = sportFitnessPhysicalAdapter
        }

        sportFitnessDocumentalAdapter = SportFitnessDocumentalAdapter(documentalList)
        binding.documentalRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = sportFitnessDocumentalAdapter
        }


    }

    private fun initCallBack() {
        bookViewModel.getEvaluationLogList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.apply {
                    physicalList.clear()
                    for (i in it.indices) {
                        physicalList.add(it[i])
                        if (i > 1) {
                            break
                        }
                    }
                    sportFitnessPhysicalAdapter.notifyDataSetChanged()
                }
            }
        }

        bookViewModel.getActivityLogList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                binding.apply {
                    documentalList.clear()
                    for (i in it.indices) {
                        documentalList.add(it[i])
                        if (i > 1) {
                            break
                        }
                    }
                    sportFitnessDocumentalAdapter.notifyDataSetChanged()
                }
            }
        }

        bookViewModel.getCouponPoint.observe(viewLifecycleOwner) {
            it?.let { point ->
                binding.apply {
                    val value = arguments?.getString("value")
                    if (value == "true") {
                        sportFitnessPointTextview.text = "您現有 ${point} 點"
                    } else {
                        sportFitnessPointTextview.text = "您現有 0點"
                    }
                }
                memberPoint = point
            }
        }
    }
}