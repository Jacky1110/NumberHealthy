package com.jotangi.NumberHealthy.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jotangi.NumberHealthy.api.book.BookViewModel
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.databinding.FragmentABinding
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.jotangi.NumberHealthy.utils.constant.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class AFragment : Fragment() {

    private lateinit var binding: FragmentABinding

    companion object {
        fun newInstance() = AFragment()
    }

    private val bookViewModel: BookViewModel by viewModel()

    private var circleData = ArrayList<CircleData>()

    private var legendTV = ArrayList<TextView>()
    private var backgroundV = ArrayList<View>()
    private var iconIV = ArrayList<ImageView>()
    private var clickCL = ArrayList<ConstraintLayout>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentABinding.inflate(inflater, container, false)
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
                tvAfC1, tvAfC2, tvAfC3, tvAfC4, tvAfC5, tvAfC6, tvAfC7, tvAfC8, tvAfC9
            )

            backgroundV = arrayListOf(
                vAfC1, vAfC2, vAfC3, vAfC4, vAfC5, vAfC6, vAfC7, vAfC8, vAfC9
            )

            iconIV = arrayListOf(
                ivAfC1, ivAfC2, ivAfC3, ivAfC4, ivAfC5, ivAfC6, ivAfC7, ivAfC8, ivAfC9
            )

            clickCL = arrayListOf(
                clAfC1, clAfC2, clAfC3, clAfC4, clAfC5, clAfC6, clAfC7, clAfC8, clAfC9
            )
        }

        val legendList = listOf(
            getString(R.string.circle_heart_rate),
            getString(R.string.circle_blood_pressure),
            getString(R.string.circle_blood_oxygen),

            getString(R.string.circle_breath_rate),
            getString(R.string.circle_body_temperature),
            getString(R.string.circle_sleep),

            getString(R.string.circle_step_count),
            getString(R.string.circle_ecg),
            getString(R.string.circle_kcal)
        )

        val skinDrawable = R.drawable.shape_circle_skin

        val iconList = listOf(
            R.drawable.icon_circle_heart_rate,
            R.drawable.icon_circle_blood_pressure,
            R.drawable.icon_circle_blood_oxygen,

            R.drawable.icon_circle_breath_rate,
            R.drawable.icon_circle_body_temperature,
            R.drawable.icon_circle_sleep,

            R.drawable.icon_circle_step_count,
            R.drawable.icon_circle_ecg,
            R.drawable.icon_circle_kcal
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

        for (i in circleData.indices) {

            legendTV[i].text = circleData[i].legend

            backgroundV[i].background = ResourcesCompat.getDrawable(
                resources, circleData[i].background, null
            )

            iconIV[i].setImageResource(circleData[i].icon)

            clickCL[i].setOnClickListener {

                val bundle = bundleOf(
                    JiugonggeEnum.LEGEND.name to circleData[i].legend,
                    JiugonggeEnum.BACKGROUND.name to circleData[i].background,
                    JiugonggeEnum.ICON.name to circleData[i].icon
                )
                findNavController().navigate(R.id.cFragment, bundle)
            }
        }
    }
}