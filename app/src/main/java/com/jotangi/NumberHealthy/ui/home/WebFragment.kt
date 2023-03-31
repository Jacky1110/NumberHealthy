package com.jotangi.NumberHealthy.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.jotangi.NumberHealthy.api.ApiConstant
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.databinding.FragmentWebBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.utils.AESCrypt.aesEncrypt
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import java.net.URLEncoder


class WebFragment : BaseFragment() {

    private lateinit var binding: FragmentWebBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private val args by navArgs<WebFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWebBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarArrow(args.title)

        val id = SharedPreferencesUtil.instances.getAccountId()
        val mid = SharedPreferencesUtil.instances.getAccountMid()
        var url = ""
        Log.d(TAG, "args.title: ${args.title}")

        when (args.title) {

            getString(R.string.circle_heart_rate) -> url = ApiConstant.HHQ_URL + mid
            getString(R.string.circle_blood_pressure) -> url = ApiConstant.HBQ_URL + mid
            getString(R.string.circle_blood_oxygen) -> url = ApiConstant.HOQ_URL + mid
            getString(R.string.circle_breath_rate) -> url = ApiConstant.HRQ_URL + mid
            getString(R.string.circle_body_temperature) -> url = ApiConstant.HTQ_URL + mid
            getString(R.string.circle_sleep) -> url = ApiConstant.HSlQ_URL + mid
            getString(R.string.circle_step_count) -> url = ApiConstant.HStQ_URL + mid

            // --------------------------------------------------------------------
            getString(R.string.circle_go_body) -> url =
                "${ApiConstant.WEB_URL}${ApiConstant.ID_GO_BODY}$id"
            getString(R.string.circle_health_rapid_test) -> {
                url = id?.aesEncrypt()?.let {
                    "${ApiConstant.QUICK_SIEVE_URL}${URLEncoder.encode(it, "UTF-8")}"
                } ?: ApiConstant.QUICK_SIEVE_URL
            }
            getString(R.string.circle_vessel_stiffness) -> url =
                "${ApiConstant.WEB_URL}${ApiConstant.ID_VESSEL_STIFFNESS}$id"
            getString(R.string.circle_physical_examination) -> {
                binding.apply {
                    webView.visibility = View.GONE
                    ivNoData.visibility = View.VISIBLE
                    tvNoData.visibility = View.VISIBLE
                }
            }
            getString(R.string.circle_eecp) -> url =
                "${ApiConstant.WEB_URL}${ApiConstant.ID_EECP}$id"
        }

        Log.d(TAG, "url: $url")
        setupWeb(url)
    }

    private fun setupWeb(url: String) {

        binding.webView.run {

            with(settings) {

                javaScriptEnabled = true
                domStorageEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        url?.let { view?.loadUrl(it) }
                        return true
                    }
                }
            }

            loadUrl(url)
        }
    }
}