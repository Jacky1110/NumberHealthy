package com.jotangi.NumberHealthy.ui.Member

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.jotangi.NumberHealthy.api.book.BookApiRepository
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.databinding.FragmentFixPasswordBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.utils.CoverPassword
import com.jotangi.NumberHealthy.utils.DialogUtils
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FixPasswordFragment : BaseFragment() {

    private lateinit var binding: FragmentFixPasswordBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private val apiRepository: BookApiRepository by lazy { BookApiRepository() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFixPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarArrow("修改密碼")
        initHandler()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initHandler() {

        binding.apply {

            etNewPwd.transformationMethod = CoverPassword()

            ivNewPwdEye.setOnTouchListener { view, motionEvent ->

                when (motionEvent.action) {

                    MotionEvent.ACTION_DOWN -> {
                        etNewPwd.transformationMethod =
                            HideReturnsTransformationMethod.getInstance()
                    }

                    MotionEvent.ACTION_UP -> {
                        etNewPwd.transformationMethod = CoverPassword()
                    }
                }

                return@setOnTouchListener true
            }


            etNewPwdAgain.transformationMethod = CoverPassword()

            ivNewPwdEyeAgain.setOnTouchListener { view, motionEvent ->

                when (motionEvent.action) {

                    MotionEvent.ACTION_DOWN -> {
                        etNewPwdAgain.transformationMethod =
                            HideReturnsTransformationMethod.getInstance()
                    }

                    MotionEvent.ACTION_UP -> {
                        etNewPwdAgain.transformationMethod = CoverPassword()
                    }
                }

                return@setOnTouchListener true
            }


            btSendFixPwd.setOnClickListener {

                val newPwd = etNewPwd.text.toString()
                val newPwdAgain = etNewPwdAgain.text.toString()

                if (newPwd != newPwdAgain) {

                    showErrorMsgDialog("密碼不一致，請確認後再次修改")

                } else {

                    showProgress()

                    lifecycleScope.launch {

                        val response = apiRepository.userChangePwd(newPwd)

                        closeProgress()

                        if (response.code != "0x0200") {

                            showErrorMsgDialog(response.responseMessage.toString())
                        } else {

                            CoroutineScope(Dispatchers.Main).launch {

                                DialogUtils.showSingle(
                                    requireActivity(),
                                    "訊息",
                                    "密碼修改成功"
                                ) {
                                    SharedPreferencesUtil.instances.setAccountPwd(newPwd)
                                    requireActivity().onBackPressed()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}