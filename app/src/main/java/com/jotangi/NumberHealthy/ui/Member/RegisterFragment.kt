package com.jotangi.NumberHealthy.ui.Member

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.api.book.BookApiRepository
import com.jotangi.NumberHealthy.databinding.FragmentRegisterBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.ui.mylittlemin.UserRegister
import com.jotangi.NumberHealthy.utils.CoverPassword
import com.jotangi.NumberHealthy.utils.DialogUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RegisterFragment : BaseFragment() {

    private lateinit var binding: FragmentRegisterBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private val apiRepository: BookApiRepository by lazy { BookApiRepository() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarArrow("註冊")
        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        binding.apply {

            ePwd.transformationMethod = CoverPassword()

            ivEye.setOnTouchListener { view, motionEvent ->

                when (motionEvent.action) {

                    MotionEvent.ACTION_DOWN -> {
                        ePwd.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    }

                    MotionEvent.ACTION_UP -> {
                        ePwd.transformationMethod = CoverPassword()
                    }
                }

                return@setOnTouchListener true
            }
            againEPwd.transformationMethod = CoverPassword()

            ivEyeNew.setOnTouchListener { view, motionEvent ->

                when (motionEvent.action) {

                    MotionEvent.ACTION_DOWN -> {
                        againEPwd.transformationMethod =
                            HideReturnsTransformationMethod.getInstance()
                    }

                    MotionEvent.ACTION_UP -> {
                        againEPwd.transformationMethod = CoverPassword()
                    }
                }

                return@setOnTouchListener true
            }



            btRe.setOnClickListener {
                memberRegister()
            }

            tvPrivacy.setOnClickListener {

                findNavController().navigate(R.id.usertermsFragment)
            }
        }
    }

    private fun memberRegister() {
        val name: String = binding.eName.text.toString()
        val phone: String = binding.ePhone.text.toString()
        val pwd: String = binding.ePwd.text.toString()
        val agpwd: String = binding.againEPwd.text.toString()
        if (name == "" || phone == "" || pwd == "" || !binding.eR.isChecked
        ) {
            CoroutineScope(Dispatchers.Main).launch {
                DialogUtils.showSingle(
                    requireActivity(),
                    "註冊失敗",
                    "請檢查欄位並勾選同意聲明",
                ) {

                }
            }
            return
        } else if (pwd != agpwd) {
            CoroutineScope(Dispatchers.Main).launch {
                DialogUtils.showSingle(
                    requireActivity(),
                    "密碼不一致",
                    "請再次檢查密碼",
                ) {

                }
            }
            return
        } else {
            showProgress()

            lifecycleScope.launch {

                val response = apiRepository.userRegister(
                    UserRegister(
                        phone,
                        pwd,
                        name,
                        "1"
                    )
                )

                closeProgress()


                if (response.code == "0x0201") {
                    CoroutineScope(Dispatchers.Main).launch {
                        DialogUtils.showSingle(
                            requireActivity(),
                            "註冊失敗",
                            "帳號已存在",
                        ) {

                        }
                    }

                } else if (response.code == "0x0200") {
                    CoroutineScope(Dispatchers.Main).launch {
                        DialogUtils.showSingle(
                            requireActivity(),
                            "註冊成功",
                            "請再次重新登入",
                        ) {
                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                        }
                    }
                } else {
                    CoroutineScope(Dispatchers.Main).launch {
                        DialogUtils.showSingle(
                            requireActivity(),
                            "註冊失敗",
                            response.responseMessage,
                        ) {

                        }
                    }
                }
            }
        }
    }
}