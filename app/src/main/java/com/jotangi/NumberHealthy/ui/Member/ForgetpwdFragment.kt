package com.jotangi.NumberHealthy.ui.Member

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.api.book.BookApiRepository
import com.jotangi.NumberHealthy.databinding.FragmentForgetpwdBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.ui.mylittlemin.UserResetPwd
import com.jotangi.NumberHealthy.utils.DialogUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForgetpwdFragment : BaseFragment() {

    private lateinit var binding: FragmentForgetpwdBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private val apiRepository: BookApiRepository by lazy { BookApiRepository() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgetpwdBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        setToolbarArrow("忘記密碼")
        binding.apply {
            fgGet.setOnClickListener { getCode() }
            fgSend.setOnClickListener { send() }
        }

    }

    fun getCode() {
        val account: String = binding.fgPhone.text.trim().toString()

        showProgress()

        lifecycleScope.launch {

            val response = apiRepository.userCode(account)

            closeProgress()

            if (response.code == "0x0200") {
                CoroutineScope(Dispatchers.Main).launch {
                    DialogUtils.showSingle(
                        requireActivity(),
                        "注意",
                        "驗證碼已傳送",
                    ) {

                    }
                }
            }
        }
    }

    fun send() {
        val number: String = binding.fgPhone.text.toString()
        val code: String = binding.fgOtp.text.toString()
        val pwd: String = binding.fgNewPwd.text.toString()
        val again: String = binding.fgAgain.text.toString()
        if (number == "" || code == "" || pwd == "" || again == "") {
            CoroutineScope(Dispatchers.Main).launch {
                DialogUtils.showSingle(
                    requireActivity(),
                    "重設密碼錯誤",
                    "欄位不得為空",
                ) {
                    binding.fgNewPwd.text.clear()
                    binding.fgAgain.text.clear()
                }
            }
            return
        }

        if (again != pwd) {

            CoroutineScope(Dispatchers.Main).launch {
                DialogUtils.showSingle(
                    requireActivity(),
                    "密碼不一致",
                    "請再次檢查密碼",
                ) {
                    binding.fgNewPwd.text.clear()
                    binding.fgAgain.text.clear()
                }
            }
            return
        }

        showProgress()

        lifecycleScope.launch {
            val response = apiRepository.userResetPwd(
                UserResetPwd(
                    number,
                    pwd,
                    code
                )
            )

            closeProgress()

            if (response.code == "0x0200") {
                CoroutineScope(Dispatchers.Main).launch {
                    DialogUtils.showSingle(
                        requireActivity(),
                        "重設密碼成功",
                        "請再次重新登入",
                    ) {
                        findNavController().navigate(R.id.loginFragment)
                    }
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    DialogUtils.showSingle(
                        requireActivity(),
                        "重設密碼失敗",
                        response.responseMessage,
                    ) {
                        binding.fgNewPwd.text.clear()
                        binding.fgOtp.text.clear()
                        binding.fgAgain.text.clear()
                    }
                }
            }
        }
    }
}