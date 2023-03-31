package com.jotangi.NumberHealthy.ui.Member

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.api.book.BookApiRepository
import com.jotangi.NumberHealthy.databinding.FragmentLoginBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.ui.mylittlemin.UserLoginRequest
import com.jotangi.NumberHealthy.utils.CommonKtUtils
import com.jotangi.NumberHealthy.utils.CoverPassword
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private val apiRepository: BookApiRepository by lazy { BookApiRepository() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        initCallBack()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {

        setToolbar("登入")

        binding.apply {

            // 會員使用條款
            LoRule.setOnClickListener { findNavController().navigate(R.id.usertermsFragment) }
            // 重設密碼
            loForget.setOnClickListener { findNavController().navigate(R.id.forgetpwdFragment) }
            // 註冊
            toRrgister.setOnClickListener { findNavController().navigate(R.id.registerFragment) }

            LoPwd.transformationMethod = CoverPassword()

            ivEye.setOnTouchListener { view, motionEvent ->

                when (motionEvent.action) {

                    MotionEvent.ACTION_DOWN -> {
                        LoPwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    }

                    MotionEvent.ACTION_UP -> {
                        LoPwd.transformationMethod = CoverPassword()
                    }
                }

                return@setOnTouchListener true
            }

            // 登入
            toLogin.setOnClickListener {

                val tel: String = binding.LoPhpne.text.toString()
                val pw: String = binding.LoPwd.text.toString()

                if (checkAccPwd(tel, pw)) {

                    showProgress()

                    lifecycleScope.launch {

                        FirebaseApp.initializeApp(requireContext())
                        var token: String?
                        FirebaseMessaging.getInstance().token.addOnCompleteListener(
                            OnCompleteListener { task ->

                                if (!task.isSuccessful) {
                                    closeProgress()
                                    return@OnCompleteListener
                                }

                                token = task.result
                                Log.w(TAG, "FCM_token: $token")

                                lifecycleScope.launch {

                                    CoroutineScope(Dispatchers.IO).launch {

                                        var adId: String?
                                        try {
                                            val adInfo = AdvertisingIdClient.getAdvertisingIdInfo(
                                                requireContext()
                                            )
                                            adId = adInfo.id
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                            adId = null
                                        }

                                        Log.w(TAG, "adId: $adId")

                                        val response =
                                            apiRepository.userLogin(
                                                UserLoginRequest(
                                                    tel,
                                                    pw,
                                                    token,
                                                    adId
                                                )
                                            )

                                        if (response.code == "0x0200") {

                                            SharedPreferencesUtil.instances.setAccountId(tel)
                                            SharedPreferencesUtil.instances.setAccountPwd(pw)

                                            closeProgress()

                                            CoroutineScope(Dispatchers.Main).launch {
                                                if (SharedPreferencesUtil.instances.getAccountId() == "53651543") {
                                                    findNavController().navigate(R.id.storeMangerFragment)
                                                } else {
                                                    findNavController().navigate(
                                                        LoginFragmentDirections.actionLoginFragmentToMemberFragment()
                                                    )
                                                }
                                            }

                                        } else {

                                            closeProgress()

                                            showErrorMsgDialog(response.responseMessage.toString())

                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }

    // 檢查 帳、密
    private fun checkAccPwd(tel: String, pw: String): Boolean {

        var isSafe = false

        when {
            tel.isEmpty() -> showErrorMsgDialog("手機號碼不能為空白。")
//            tel.length != 10 -> showErrorMsgDialog("手機號碼長度錯誤。")
            pw.isEmpty() -> showErrorMsgDialog("密碼不能為空白。")
            pw.length < 6 -> showErrorMsgDialog("請輸入 6～12碼 英數字混合")
            pw.length > 12 -> showErrorMsgDialog("請輸入 6～12碼 英數字混合")
            else -> isSafe = true
        }

        return isSafe
    }

    private fun initCallBack() {

        bookViewModel.playStoreVersion.observe(viewLifecycleOwner) {

            Log.d(TAG, "playStoreVersion: $it")
            CommonKtUtils.instance.checkVersion(it) {

                showUpVersion()
            }
        }
    }
}