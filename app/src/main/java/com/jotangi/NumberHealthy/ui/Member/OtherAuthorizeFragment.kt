package com.jotangi.NumberHealthy.ui.Member

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.NumberHealthy.api.book.CareListVO
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.databinding.FragmentOtherAuthorizeBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.utils.DialogUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OtherAuthorizeFragment : BaseFragment(),
    CareEditClickListener,
    CareDeleteClickListener,
    CaredCancelClickListener {

    private lateinit var binding: FragmentOtherAuthorizeBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private lateinit var careAdapter: CareAdapter
    private lateinit var caredAdapter: CaredAdapter
    private var careData = mutableListOf<CareListVO>()
    private var caredData = mutableListOf<CareListVO>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOtherAuthorizeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
        initData()
        initView()
    }

    private fun initObserver() {

        bookViewModel.careAuthMessageLiveData.observe(viewLifecycleOwner) { result ->
            if (result.isNotEmpty()) {
                showResultDialog(result)
            }
        }

        bookViewModel.careList.observe(viewLifecycleOwner) { result ->
            careData = if (result != null && result.isNotEmpty()) {
                result
            } else {
                mutableListOf()
            }

            careAdapter.updateDataSource(careData)
        }

        bookViewModel.caredList.observe(viewLifecycleOwner) { result ->
            caredData = if (result != null && result.isNotEmpty()) {
                result
            } else {
                mutableListOf()
            }

            caredAdapter.updateDataSource(caredData)
            bookViewModel.careMemberCancelResult.postValue("")
        }

        bookViewModel.careNickNameUpdateResult.observe(viewLifecycleOwner) { result ->
            if (result.isNotEmpty()) {
                when (result) {
                    "0x0200" -> {
                        lifecycleScope.launch {
                            bookViewModel.getCareList()
                        }
                    }

                    "0x0201" -> {
                        careData.clear()

                        careAdapter.updateDataSource(careData)
                    }
                }
            }
        }

        bookViewModel.careMemberDeleteResult.observe(viewLifecycleOwner) { result ->
            if (result.isNotEmpty()) {
                when (result) {
                    "0x0200" -> {
                        lifecycleScope.launch {
                            bookViewModel.getCareList()
                        }
                    }

                    "0x0201" -> {
                        careData.clear()
                        requireActivity().runOnUiThread {
                            careAdapter.updateDataSource(careData)
                        }
                    }
                }
            }
        }

        bookViewModel.careMemberCancelResult.observe(viewLifecycleOwner) { result ->
            if (result.isNotEmpty()) {
                when (result) {
                    "0x0200" -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            bookViewModel.getCaredList()
                        }
                    }

                    "0x0201" -> {
                        caredData.clear()
                        requireActivity().runOnUiThread {
                            caredAdapter.updateDataSource(caredData)
                        }

                        bookViewModel.careMemberCancelResult.postValue("")
                    }
                }
            }
        }
    }

    private fun showResultDialog(result: String) {

        CoroutineScope(Dispatchers.Main).launch {

            DialogUtils.showMyDialog(
                requireActivity(),
                "提醒",
                result,
                "確認"
            ) {
                CoroutineScope(Dispatchers.IO).launch {
                    bookViewModel.getCareList()
                }

                bookViewModel.careAuthMessageLiveData.postValue("")
            }
        }
    }

    private fun initData() {
        lifecycleScope.launch {
            bookViewModel.getCareList()
            bookViewModel.getCaredList()
        }
    }

    private fun initView() {

        setToolbarArrow("授權設定")

        binding.apply {

            // 授權申請
            btAuthorizeInvite.setOnClickListener {

                if (checkId(etAccountEntry.text.toString())) {
                    lifecycleScope.launch {
                        bookViewModel.addCarelist(etAccountEntry.text.toString())
                    }
                } else {

                    showErrorMsgDialog("請輸入正確的手機號碼。")
                }
            }

            // 我關注的
            rvCared.apply {
                layoutManager = LinearLayoutManager(requireContext())
                careAdapter = CareAdapter(
                    careData,
                    this@OtherAuthorizeFragment,
                    this@OtherAuthorizeFragment
                )
                this.adapter = careAdapter
            }

            // 關注我的
            rvBeCared.apply {
                layoutManager = LinearLayoutManager(requireContext())
                caredAdapter = CaredAdapter(
                    caredData,
                    this@OtherAuthorizeFragment
                )
                this.adapter = caredAdapter
            }
        }
    }

    private fun checkId(id: String): Boolean {

        return when {
            id.length != 10 -> false
            !id.substring(0, 2).contains("09") -> false
            else -> true
        }
    }

    override fun onCareEditClick(vo: CareListVO) {
        showNickNameUpdateDialog(vo)
    }

    override fun onCareDeleteClick(vo: CareListVO) {

        CoroutineScope(Dispatchers.Main).launch {

            DialogUtils().showMultiple(
                requireActivity(),
                "確認刪除",
                "請您確認是否取消對\n${vo.nick_name}(${vo.cmember_id})的關注",
                "是",
                "否",
                object : DialogUtils.OnMultipleClickListener{
                    override fun onOk() {

                        vo.cid?.let {

                            lifecycleScope.launch {

                                bookViewModel.deleteCareMember(it)
                            }
                        }
                    }

                    override fun onCancel() {}

                }
            )
        }
    }

    override fun onCaredCancelClick(vo: CareListVO) {

        CoroutineScope(Dispatchers.Main).launch {

            DialogUtils().showMultiple(
                requireActivity(),
                "確認取消",
                "請您確認是否取消\n${vo.fmember_name}(${vo.fmember_id})對您的授權",
                "是",
                "否",
                object : DialogUtils.OnMultipleClickListener{
                    override fun onOk() {

                        shortNews(vo.cid)
                    }

                    override fun onCancel() {}

                }
            )
        }
    }

    private fun shortNews(cid: String?) {

        DialogUtils().showMultiple(
            requireActivity(),
            "",
            "是否發送簡訊通知?提醒您，無論您選擇是否發送，雙方數體健的「訊息」中，皆會保留取消授權的歷程記錄。",
            "是",
            "否",
            object : DialogUtils.OnMultipleClickListener{
                override fun onOk() {

                    CoroutineScope(Dispatchers.IO).launch {
                        cid?.let {
                            bookViewModel.cancelCaredMember(it, "1")
                        }
                    }
                }

                override fun onCancel() {

                    CoroutineScope(Dispatchers.IO).launch {
                        cid?.let {
                            bookViewModel.cancelCaredMember(it, "")
                        }
                    }
                }

            }
        )
    }

    private fun showNickNameUpdateDialog(vo: CareListVO) {
        requireActivity().runOnUiThread {
            val alert = AlertDialog.Builder(requireContext())
            val editText = EditText(requireContext())
            editText.isSingleLine = true

            alert.setTitle("會員帳號 ${vo.cmember_id}")
            alert.setMessage("請編輯帳號暱稱")
            alert.setView(editText)

            alert.setPositiveButton("確定") { _, _ ->
                lifecycleScope.launch {
                    bookViewModel.updateCareNickName(
                        editText.text.toString(),
                        vo.cid!!
                    )
                }
            }

            alert.setNegativeButton("取消") { _, _ ->
                // what ever you want to do with No option.
            }

            alert.show()
        }
    }
}