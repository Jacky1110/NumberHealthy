package com.jotangi.NumberHealthy.ui.Member

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.api.book.BookApiRepository
import com.jotangi.NumberHealthy.databinding.FragmentInformationBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.ui.mylittlemin.UserEditRequest
import com.jotangi.NumberHealthy.utils.DialogUtils
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.jotangi.NumberHealthy.utils.SpinnerDatePickerDialog
import com.jotangi.NumberHealthy.utils.ViewUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class InformationFragment : BaseFragment() {

    private lateinit var binding: FragmentInformationBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private val apiRepository: BookApiRepository by lazy { BookApiRepository() }

    // 性別
    private var gender = "0"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarArrow("會員資料")

        initCallBack()
        initHandler()
    }

    private fun initCallBack() {

        bookViewModel.memberInfoLD.observe(viewLifecycleOwner) { data ->

            data.code ?: return@observe

            when(data.code) {
                "0x0205" -> {

                    SharedPreferencesUtil.instances.setAccountId("")
                    SharedPreferencesUtil.instances.setAccountPwd("")
                    goLogin()
                }

                "0x0200" -> {

                    data.list?.let {
                        binding.apply {

                            edName.setText(it[0].member_name)

                            rgSex.check(if (it[0].member_gender == "0") R.id.rWomen else R.id.rMan)

                            tvBirth.text = it[0].member_birthday

                            tvPhone.text = it[0].member_id

                            edEmail.setText(it[0].member_email)
                        }
                    }
                }
            }
        }
    }

    private fun initHandler() {

        binding.apply {
            // 性別
            rgSex.setOnCheckedChangeListener { group, checkedId ->
                when (checkedId) {
                    rWomen.id -> {
                        gender = "0"
                    }
                    rMan.id -> {
                        gender = "1"
                    }
                }
            }

            // 生日
            tvBirth.setOnClickListener {
                showDatePickerDialog()
            }

            // 修改密碼
            btFixPwd.setOnClickListener {
                findNavController().navigate(R.id.fixPasswordFragment)
            }

            // 修改資料
            btFix.setOnClickListener {

                if (edName.text.isBlank()) {

                    showErrorMsgDialog("姓名不能為空白")

                } else {
                    userEditProcess()
                }
            }
        }
    }

    // 修改會員資料
    private fun userEditProcess() {

        showProgress()
        lifecycleScope.launch {

            val response = apiRepository.userEdit(
                UserEditRequest(
                    binding.edName.text.toString(),
                    gender,
                    binding.edEmail.text.toString(),
                    binding.tvBirth.text.toString(),
                    "null",
                    null
                )
            )

            closeProgress()

            if (response.code == "0x0200") {

                CoroutineScope(Dispatchers.Main).launch {
                    DialogUtils.showSingle(
                        requireActivity(),
                        "",
                        "資料修改成功"
                    ) {
                        findNavController().navigate(R.id.action_informationFragment_to_memberFragment)
                    }
                }
            }
        }
    }

    private fun showDatePickerDialog() {

        val calendar = Calendar.getInstance()
        val datePickerDialog: DatePickerDialog

        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.N) {
            datePickerDialog = SpinnerDatePickerDialog(
                requireActivity(),
                R.style.DatePickerTheme,
                DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->

                    Log.d(TAG, "SpinnerDate: $i $i1 $i2")
                    var month = (i1 + 1).toString()
                    if (month.length < 2) {
                        month = "0$month"
                    }
                    var day = i2.toString()
                    if (day.length < 2) {
                        day = "0$day"
                    }
                    binding.tvBirth.text = "${i}-${month}-${day}"
                },
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            )
        } else {
            datePickerDialog = DatePickerDialog(
                requireActivity(),
                R.style.DatePickerTheme,
                DatePickerDialog.OnDateSetListener { datePicker, i, i1, i2 ->

                    Log.d(TAG, "date: $i $i1 $i2")
                    var month = (i1 + 1).toString()
                    if (month.length < 2) {
                        month = "0$month"
                    }
                    var day = i2.toString()
                    if (day.length < 2) {
                        day = "0$day"
                    }
                    binding.tvBirth.text = "${i}-${month}-${day}"
                },
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            )
        }
        ViewUtils.colorizeDatePicker(datePickerDialog.datePicker)
        datePickerDialog.datePicker.spinnersShown = true
        datePickerDialog.datePicker.calendarViewShown = false
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }
}