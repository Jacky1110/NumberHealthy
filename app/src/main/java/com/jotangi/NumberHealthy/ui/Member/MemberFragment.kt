package com.jotangi.NumberHealthy.ui.Member


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.R
import com.jotangi.NumberHealthy.api.ApiConstant
import com.jotangi.NumberHealthy.api.book.BookApiRepository
import com.jotangi.NumberHealthy.api.book.MemberInfoData
import com.jotangi.NumberHealthy.databinding.FragmentMemberBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.utils.*
import com.jotangi.NumberHealthy.utils.smartwatch.WatchUtils
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileInputStream

class MemberFragment : BaseFragment() {

    private lateinit var binding: FragmentMemberBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private val apiRepository: BookApiRepository by lazy { BookApiRepository() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMemberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initApi()
        initView()
        initHandler()
        initCallBack()
    }

    private fun initApi() {
        lifecycleScope.launch {
            bookViewModel.memberInfo()
        }
    }

    private fun initView() {
        setToolbar("會員中心")
    }

    private fun initHandler() {

        binding.apply {

            sivPhoto.setOnClickListener {
                val intent = Intent(requireActivity(), CropHeadImageActivity::class.java)
                getUserHeadLauncher.launch(intent)
            }

            //會員
            tvMemberQR.setOnClickListener { findNavController().navigate(R.id.memberQrFragment) }

            // 會員資料
            tvMemberInfo.setOnClickListener { findNavController().navigate(R.id.informationFragment) }

            // 裝置設定
            tvWatch.setOnClickListener {

                if (SharedPreferencesUtil.instances.getWatchMac().isNullOrBlank()) {
                    findNavController().navigate(R.id.watchListFragment)
                } else {
                    findNavController().navigate(R.id.watchSettingFragment)
                }
            }

            // 使用者條款
            tvUserterms.setOnClickListener { findNavController().navigate(R.id.usertermsFragment) }

            // 登出
            tvLogout.setOnClickListener {

                lifecycleScope.launch {
                    showProgress()
                    val response = apiRepository.userLogout()
                    closeProgress()
                    if (!response.code.isNullOrBlank()) {
                        SharedPreferencesUtil.instances.setAccountId(null)
                        SharedPreferencesUtil.instances.setAccountPwd(null)
                        SharedPreferencesUtil.instances.setAccountMid(null)
                        SharedPreferencesUtil.instances.setWatchMac(null)
                        SharedPreferencesUtil.instances.setWatchName(null)
                        WatchUtils.instance.disconnectBle()
                        watchViewModel.setIsBleConnect(false)
                        bookViewModel.clearData()
                        FirebaseMessaging.getInstance().deleteToken()
                        CoroutineScope(Dispatchers.Main).launch {
                            findNavController().navigate(
                                MemberFragmentDirections.actionMemberFragmentToLoginFragment()
                            )
                        }
                    }
                }
            }

            // 授權設定
            tvOtherAuthorize.setOnClickListener {
                findNavController().navigate(R.id.otherAuthorizeFragment)
            }
        }
    }

    private var getUserHeadLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                val path = data.getStringExtra("path")
                SharedPreferencesUtil.instances.setAccountHeadShot(path)
                Log.d(TAG,"大頭照路徑: $path")
                watchViewModel.refreshHeadShotPath()
                if (path != null) {
                    reloadUserHeadShot(path)
                }
            }
        }
    }

    private fun reloadUserHeadShot(path: String?) {
        //Glide.with(requireContext()).load(path?:R.drawable.ic_img_user).into(binding.sivPhoto)
        if (path != null) {
            binding.sivPhoto.setImageBitmap(loadAndRotateBitmap(path))
            userUploadPicProcess(path)

        } else {
            binding.sivPhoto.setImageResource(R.drawable.ic_img_user)
        }
    }

    private fun loadAndRotateBitmap(photoFilePath: String?): Bitmap? {
        return try {
            var fis = FileInputStream(photoFilePath)
            val angle =
                BitmapUtils.getRotateAngleFromImageFile(fis)
            fis.close()
            fis = FileInputStream(photoFilePath)
            val bounds =
                BitmapUtils.getBitmapBounds(fis)
            fis.close()
            fis = FileInputStream(photoFilePath)
            val bmp = BitmapUtils.loadBitmap(fis)
            fis.close()
            BitmapUtils.rotateBitmap(bmp, angle, bounds.outWidth, bounds.outHeight)
        } catch (var6: Exception) {
            var6.printStackTrace()
            null
        }
    }

    // 上傳 大頭照到後台
    private fun userUploadPicProcess(path: String) {
        lifecycleScope.launch {
            apiRepository.userUploadpic(path)
        }
    }

    private fun initCallBack() {

        bookViewModel.playStoreVersion.observe(viewLifecycleOwner) {

            Log.d(TAG, "playStoreVersion: $it")
            CommonKtUtils.instance.checkVersion(it) {

                showUpVersion()
            }
        }

        bookViewModel.memberInfoLD.observe(viewLifecycleOwner) {data ->

            data.code ?: return@observe

            when(data.code) {

                "0x0200" -> {

                    data.list?.let {
                        Picasso.get().load(ApiConstant.MUG_SHOT_URL + it[0].member_picture)
                            .into(binding.sivPhoto)

                        SharedPreferencesUtil.instances.setAccountMid(it[0].mid)
                    }
                }

                "0x0205" -> {

                    SharedPreferencesUtil.instances.setAccountId("")
                    SharedPreferencesUtil.instances.setAccountPwd("")
                    goLogin()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bookViewModel.memberInfoLD.value = MemberInfoData()
    }
}