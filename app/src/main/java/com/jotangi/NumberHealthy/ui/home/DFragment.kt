package com.jotangi.NumberHealthy.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jotangi.NumberHealthy.api.book.BookApiRepository
import com.jotangi.NumberHealthy.api.book.BookViewModel
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.databinding.FragmentDBinding
import com.jotangi.NumberHealthy.utils.DateUtil
import com.jotangi.NumberHealthy.utils.DialogUtils
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.jotangi.NumberHealthy.utils.smartwatch.WatchUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DFragment : Fragment() {

    private lateinit var binding: FragmentDBinding

    companion object {
        fun newInstance() = DFragment()
    }

    private val bookViewModel: BookViewModel by viewModel()

    private val apiRepository: BookApiRepository by lazy { BookApiRepository() }

    private var circleData = ArrayList<CircleData>()

    private var legendTV = ArrayList<TextView>()
    private var backgroundV = ArrayList<View>()
    private var iconIV = ArrayList<ImageView>()
    private var clickCL = ArrayList<ConstraintLayout>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initData()
        initView()
    }

    private fun initData() {
        binding.apply {
            legendTV = arrayListOf(
                fitnessMirrorTextview
            )
            backgroundV = arrayListOf(
                fitnessMirrorView
            )
            iconIV = arrayListOf(
                fitnessMirrorImageView
            )

            clickCL = arrayListOf(
                fitnessMirrorConstraintLayout
            )

        }


        val legendList = listOf(
            getString(R.string.circle_fitness_mirror)
        )

        val skinDrawable = R.drawable.shape_circle_skin_green

        val iconList = listOf(
            R.drawable.icon_circle_fitness_mirror,
        )

        circleData.clear()

        for (i in legendList.indices) {
            circleData.add(CircleData(legendList[i], iconList[i], skinDrawable))
        }

        if (!SharedPreferencesUtil.instances.getAccountId().isNullOrBlank()) {
            CoroutineScope(Dispatchers.IO).launch {
                bookViewModel.getCareList()
            }
        }
    }

    private fun initView() {

        val startTime = DateUtil.instance.ago3MonthYmdHms()
        val endTime = DateUtil.instance.currentYmdHms()

        for (i in circleData.indices) {
            legendTV[i].text = circleData[i].legend

            backgroundV[i].background = ResourcesCompat.getDrawable(
                resources, circleData[i].background, null
            )

            iconIV[i].setImageResource(circleData[i].icon)

            clickCL[i].setOnClickListener {

                if (SharedPreferencesUtil.instances.getAccountId().isNullOrBlank()) {
                    return@setOnClickListener
                }

                lifecycleScope.launch {
                    showProgress()
                    val response = apiRepository.getCouponPoint()
                    if (response.code == "0x0200") {

                        bookViewModel.getCouponPoint(
                            success = {
                                lifecycleScope.launch {
                                    bookViewModel.getCouponList()
                                    bookViewModel.getEvaluationLogList(startTime, endTime)
                                    bookViewModel.getActivityLogList(startTime, endTime)
                                    CoroutineScope(Dispatchers.Main).launch {
                                        DialogUtils.closeProgress()
                                        val bundle = bundleOf("value" to "true")
                                        findNavController().navigate(
                                            R.id.sportFitnessFragment,
                                            bundle
                                        )
                                    }
                                }
                            },
                            fail = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    DialogUtils.closeProgress()
                                }
                            })
                    } else {
                        lifecycleScope.launch {
                            bookViewModel.getCouponList()
                            bookViewModel.getEvaluationLogList(startTime, endTime)
                            bookViewModel.getActivityLogList(startTime, endTime)
                            CoroutineScope(Dispatchers.Main).launch {
                                DialogUtils.closeProgress()
                                val bundle = bundleOf("value" to "false")
                                findNavController().navigate(
                                    R.id.sportFitnessFragment,
                                    bundle
                                )
                            }
                        }


                    }
                }
            }
        }
    }

    fun showProgress() {
        CoroutineScope(Dispatchers.Main).launch {
            DialogUtils.showProgress(requireActivity())
        }
    }

}