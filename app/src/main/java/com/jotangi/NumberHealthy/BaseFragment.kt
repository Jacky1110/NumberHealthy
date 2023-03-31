package com.jotangi.NumberHealthy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.jotangi.NumberHealthy.api.ApiConstant
import com.jotangi.NumberHealthy.api.book.BookViewModel
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.ui.home.HomeFragmentDirections
import com.jotangi.NumberHealthy.utils.DialogUtils
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.jotangi.NumberHealthy.api.watch.WatchViewModel
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


abstract class BaseFragment : Fragment() {

    val TAG: String = "(TAG)${javaClass.simpleName}"

    abstract fun getToolBar(): ToolbarBinding?

    var mActivity: MainActivity? = null
    val watchViewModel: WatchViewModel by viewModel()
    val bookViewModel: BookViewModel by viewModel()

    private val key = "YcL+NyCRl5FYMWhozdV5V8eu6qv3cLDL".toByteArray(Charsets.UTF_8)
    private val type = "CBC"
    private val size = 256
    private val iv = "53758995@jotangi".toByteArray(Charsets.UTF_8)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            mActivity = (activity as MainActivity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setupToolbarWithCount(count: String) {
        getToolBar()?.apply {

            val notifyImageId = getImageId(count)
            setupToolbarBtn(ivToolBtn1, notifyImageId) {
                findNavController().navigate(R.id.notification_history_fragment)
            }
        }
    }

    private fun getImageId(count: String): Int {
        return when (count) {
            "0" -> R.drawable.ic_notify
            else -> R.drawable.ic_notify_active
        }
    }


    // ----------------------------------------------------------------------


    fun setToolbar(title: String) {

        getToolBar()?.apply { tvToolTitle.text = title }
    }

    fun setToolbarWatchStatus(boolean: Boolean) {

        getToolBar()?.apply {

            if (boolean) {
                iv_watch_connect_status.setImageResource(
                    R.drawable.ic_watch_connect_status_change_white
                )
            } else {
                iv_watch_connect_status.setImageResource(
                    R.drawable.ic_watch_connect_status_change_red
                )
            }

            iv_watch_connect_status.setOnClickListener {

                when {
                    SharedPreferencesUtil.instances.getAccountId().isNullOrBlank() -> {
                        findNavController().navigate(
                            HomeFragmentDirections.actionNavigationHomeToLoginFragment()
                        )
                    }

                    SharedPreferencesUtil.instances.getWatchName().isNullOrBlank() -> {
                        watchViewModel.setIsBleConnect(false)
                        findNavController().navigate(R.id.watchListFragment)
                    }
                    else -> {
                        watchViewModel.setIsBleConnect(true)
                        findNavController().navigate(R.id.watchSettingFragment)
                    }
                }
            }
        }
    }

    fun setToolbarArrow(title: String) {

        getToolBar()?.apply {

            tvToolTitle.text = title

            setupToolbarBtn(ivToolBack, R.drawable.ic_left_arrow) {
                onBackPressed()
            }
        }
    }

    fun setOutToolbarArrow(title: String) {
        getToolBar()?.apply {
            tvToolTitle.text = title
        }
    }

    fun setToolbarArrowTrend(title: String, tel: String, nav: NavDirections) {

        getToolBar()?.apply {

            tvToolTitle.text = title + getString(R.string.title_detsil)
            setupToolbarBtn(ivToolBack, R.drawable.ic_left_arrow) {
                onBackPressed()
            }

            if (SharedPreferencesUtil.instances.getAccountId() == tel) {
                tvRightContent.visibility = View.VISIBLE
                tvRightContent.setOnClickListener {
                    findNavController().navigate(nav)
                }
            } else {
                setToolbarArrow(title + getString(R.string.title_detsil))
            }
        }
    }

    fun setToolbarDetail(title: String, byId: Int) {
        getToolBar()?.apply {
            tvToolTitle.text = title
            setupToolbarBtn(ivToolBack, R.drawable.ic_close) {
                onBackPressed()
            }

            setupToolbarBtn(ivToolBtn1, R.drawable.ic_detail) {
                findNavController().navigate(byId)
            }
        }
    }

    fun setToolbarDetail(title: String, nav: NavDirections) {
        getToolBar()?.apply {
            tvToolTitle.text = title
            setupToolbarBtn(ivToolBack, R.drawable.ic_close) {
                onBackPressed()
            }

            setupToolbarBtn(ivToolBtn1, R.drawable.ic_detail) {
                findNavController().navigate(nav)
            }
        }
    }


    // ----------------------------------------------------------------------


    // toolbar 左邊 iv 元件、圖、動作
    fun setupToolbarBtn(iv: ImageView?, res: Int?, onClick: () -> Unit) {
        iv?.apply {
            visibility = View.VISIBLE
            res?.let { setImageResource(it) }
            setOnClickListener {
                onClick.invoke()
            }
        }
    }


    // 返回箭頭、叉叉
    fun onBackPressed() {
        mActivity?.onBackPressed()
    }

    fun showProgress() {
        CoroutineScope(Dispatchers.Main).launch {
            DialogUtils.showProgress(requireActivity())
        }
    }

    fun closeProgress() {
        CoroutineScope(Dispatchers.Main).launch {
            DialogUtils.closeProgress()
        }
    }

    fun showErrorMsgDialog(content: String) {
        CoroutineScope(Dispatchers.Main).launch {
            DialogUtils.showSingle(requireActivity(), "錯誤提醒", content) {}
        }
    }

    fun showErrorMsgBack(content: String) {
        CoroutineScope(Dispatchers.Main).launch {
            DialogUtils.showSingle(requireActivity(), "錯誤提醒", content) {
                onBackPressed()
            }
        }
    }

    fun showUpVersion() {

        CoroutineScope(Dispatchers.Main).launch {

            DialogUtils().showMultiple(
                requireActivity(),
                "更新提醒",
                "已有最新數體健版本\n請更新後再次使用",
                "前往",
                "關閉",
                object : DialogUtils.OnMultipleClickListener {
                    override fun onOk() {

                        SharedPreferencesUtil.instances.setAccountId(null)
                        SharedPreferencesUtil.instances.setAccountPwd(null)
                        SharedPreferencesUtil.instances.setAccountMid(null)
                        bookViewModel.clearData()

                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(ApiConstant.STORE_URL)
                            )
                        )
                    }

                    override fun onCancel() {

                        requireActivity().finishAffinity()
                    }
                }
            )
        }
    }


    // ----------------------------------------------------------------------


    fun setupToolBookStep0() {
        getToolBar()?.apply {
            tvToolTitle.text = "服務據點"
            setupToolbarBtn(ivToolBack, R.drawable.ic_left_arrow) {
                onBackPressed()
            }
        }
    }

    fun setupToolBookDetail() {
        getToolBar()?.apply {
            tvToolTitle.text = "預約詳情"
            setupToolbarBtn(ivToolBack, R.drawable.ic_left_arrow) {
                onBackPressed()
            }
        }
    }

    fun String.deAes(): String {

        return try {

            val random = SecureRandom.getInstance("SHA1PRNG")
            random.setSeed(key)
            KeyGenerator.getInstance("AES").init(size, random)

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(
                Cipher.DECRYPT_MODE,
                SecretKeySpec(key, type),
                IvParameterSpec(iv)
            )
            String(
                cipher.doFinal(
                    Base64.decode(
                        this.toByteArray(Charsets.UTF_8),
                        Base64.DEFAULT
                    )
                ),
                Charsets.UTF_8
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun String.enAes(): String {

        return try {

            val random = SecureRandom.getInstance("SHA1PRNG")
            random.setSeed(key)
            KeyGenerator.getInstance("AES").init(size, random)

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(
                Cipher.ENCRYPT_MODE,
                SecretKeySpec(key, type),
                IvParameterSpec(iv)
            )

            Base64.encodeToString(
                cipher.doFinal(this.toByteArray(Charsets.UTF_8)), Base64.DEFAULT
            )
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun goLogin() {

        activity?.let {

            val navController = it.findNavController(
                R.id.nav_host_fragment_activity_main)

            for (i in navController.backStack.indices) {
                navController.popBackStack()
            }

            navController.navigate(R.id.loginFragment)
        }
    }
}