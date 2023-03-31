package com.jotangi.NumberHealthy.ui.home

import android.os.Bundle
import android.util.Log
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
import com.jotangi.NumberHealthy.api.ApiConnect
import com.jotangi.NumberHealthy.api.ApiConstant
import com.jotangi.NumberHealthy.api.book.BookViewModel
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.databinding.FragmentBBinding
import com.jotangi.NumberHealthy.utils.DialogUtils
import com.jotangi.NumberHealthy.utils.IoUtils
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.jotangi.NumberHealthy.utils.constant.JiugonggeEnum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.File


class BFragment : Fragment() {

    private val TAG = "(TAG)${javaClass.simpleName}"

    private lateinit var binding: FragmentBBinding

    companion object {
        fun newInstance() = BFragment()
    }

    private var circleData = ArrayList<CircleData>()

    private lateinit var legendTV: ArrayList<TextView>
    private lateinit var backgroundV: ArrayList<View>
    private lateinit var iconIV: ArrayList<ImageView>
    private lateinit var clickCL: ArrayList<ConstraintLayout>

    private val bookViewModel: BookViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBBinding.inflate(inflater, container, false)
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
                tvBfC1, tvBfC2, tvBfC3, tvBfC4, tvBfC5, tvBfC6, tvBfC7, tvBfC8, tvBfC9
            )

            backgroundV = arrayListOf(
                vBfC1, vBfC2, vBfC3, vBfC4, vBfC5, vBfC6, vBfC7, vBfC8, vBfC9
            )

            iconIV = arrayListOf(
                ivBfC1, ivBfC2, ivBfC3, ivBfC4, ivBfC5, ivBfC6, ivBfC7, ivBfC8, ivBfC9
            )

            clickCL = arrayListOf(
                clBfC1, clBfC2, clBfC3, clBfC4, clBfC5, clBfC6, clBfC7, clBfC8, clBfC9
            )
        }

        val legendList = listOf(
            getString(R.string.circle_go_body),
            getString(R.string.circle_health_rapid_test),
            getString(R.string.circle_vessel_stiffness),

            getString(R.string.circle_physical_examination),
            getString(R.string.circle_eecp),
            getString(R.string.circle_foot_pressure_measurement),

            getString(R.string.circle_arm_blood_pressure),
            getString(R.string.circle_macular_pigment),
            getString(R.string.circle_bone_density)
        )

        val skinDrawable = R.drawable.shape_circle_red_skin

        val iconList = arrayListOf(
            R.drawable.icon_circle_go_body,
            R.drawable.icon_circle_health_rapid_test,
            R.drawable.icon_circle_vessel_stiffness,

            R.drawable.icon_circle_physical_examination,
            R.drawable.icon_circle_eecp,
            R.drawable.icon_circle_foot_pressure_measurement,

            R.drawable.icon_circle_arm_blood_pressure,
            R.drawable.icon_circle_macular_pigment,
            R.drawable.icon_circle_bone_density
        )

        circleData.clear()

        for (i in legendList.indices) {
            circleData.add(CircleData(legendList[i], iconList[i], skinDrawable))
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

                when (i) {

                    in 0..4 -> findNavController().navigate(
                        HomeFragmentDirections.actionNavigationHomeToWebFragment(
                            circleData[i].legend
                        )
                    )

                    5 -> {

                        val tel = SharedPreferencesUtil.instances.getAccountId()

                        bookViewModel.setFpmList(JSONArray())

                        if (!tel.isNullOrBlank()) {

                            CoroutineScope(Dispatchers.IO).launch {

                                showProgress()

                                Log.d(TAG,"足壓量測")
                                getDataByMobile(tel)
                            }
                        } else {

                            findNavController().navigate(R.id.fpmFragment)
                        }
                    }

                    in 6..8 -> {
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
    }

    private fun getDataByMobile(tel: String) {

        // 足壓量測 0956658588、0958858011、0919917279
        ApiConnect().getDataByMobile(
            tel,
            object : ApiConnect.resultListener {
                override fun onSuccess(message: String?) {

                    try {

                        val jsonArray = JSONArray(message)

                        if (jsonArray.length() > 0) {

                            val latestUrl = ApiConstant.ASIAFOOT_PDF_URL +
                                    ApiConstant.MAIN_FOOTSIZE +
                                    jsonArray.getJSONObject(0).getString("pdf")
                            Log.d(TAG, "latestUrl: $latestUrl")

                            val file = File(requireActivity().filesDir, "fpm.png")
                            val isLatest = IoUtils.pictureUrlToFile(latestUrl, file.path)
                            Log.e(TAG, "最新圖檔: $isLatest")

                            closeProgress()

                            CoroutineScope(Dispatchers.Main).launch {

                                bookViewModel.setFpmList(jsonArray)
                                findNavController().navigate(R.id.fpmFragment)
                            }
                        }

                    } catch (e: Exception) {
                        closeProgress()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(task: String?, message: String?) {
                    closeProgress()
                    Log.e(TAG, "task: $task message: $message")

                    CoroutineScope(Dispatchers.Main).launch {
                        findNavController().navigate(R.id.fpmFragment)
                    }
                }

            }
        )
    }

    private fun showProgress() {
        CoroutineScope(Dispatchers.Main).launch {
            DialogUtils.showProgress(requireActivity())
        }
    }

    private fun closeProgress() {
        CoroutineScope(Dispatchers.Main).launch {
            DialogUtils.closeProgress()
        }
    }
}