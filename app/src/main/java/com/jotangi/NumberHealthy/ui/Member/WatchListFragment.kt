package com.jotangi.NumberHealthy.ui.Member

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.databinding.FragmentWatchListBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.utils.DialogUtil
import com.jotangi.NumberHealthy.utils.DialogUtils
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil
import com.jotangi.NumberHealthy.utils.smartwatch.WatchUtils
import com.yucheng.ycbtsdk.Constants
import com.yucheng.ycbtsdk.bean.ScanDeviceBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WatchListFragment : BaseFragment() {

    private lateinit var binding: FragmentWatchListBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    private val deviceList = arrayListOf<ScanDeviceBean>()
    private lateinit var watchAdapter: WatchAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWatchListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initHandler()
    }

    private fun initView() {

        Toast.makeText(requireContext(), "請確認是否已開啟定位，才能搜尋到藍牙裝置", Toast.LENGTH_SHORT).show()

        setToolbarArrow("搜尋裝置")

        watchAdapter = WatchAdapter(deviceList)

        binding.rvWatch.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = watchAdapter
        }
    }

    private fun initHandler() {

        watchAdapter.watchItemClick = {

            DialogUtil.instance.loadingShow(requireActivity()) { dialog ->

                var isDialog = true

                CoroutineScope(Dispatchers.IO).launch {

                    Thread.sleep(5000)
                    if (isDialog) {
                        CoroutineScope(Dispatchers.Main).launch {
                            dialog.dismiss()
                        }
                    }
                }

                lifecycleScope.launch {

                    CoroutineScope(Dispatchers.IO).launch {

                        WatchUtils.instance.stopScanBle()

                        WatchUtils.instance.connectBle(it.deviceMac) { code ->

                            isDialog = false

                            Log.d(TAG, "藍牙連線回傳碼: $code")
                            if (code == Constants.CODE.Code_OK)
                                bleSuccess(it, dialog) else bleFail(dialog)
                        }
                    }
                }
            }
        }

        binding.tvRescan.apply {
            setOnClickListener {
                checkGps()
            }
        }
    }

    private fun bleSuccess(bean: ScanDeviceBean, dialog: Dialog) {

        Log.d(TAG, "藍牙連線成功")
        SharedPreferencesUtil.instances.setWatchName(bean.deviceName)
        SharedPreferencesUtil.instances.setWatchMac(bean.deviceMac)
        watchViewModel.setIsBleConnect(true)

        CoroutineScope(Dispatchers.Main).launch {
            Log.w(TAG, "actionWatchListFragmentToWatchSettingFragment")
            findNavController().navigate(
                WatchListFragmentDirections.actionWatchListFragmentToWatchSettingFragment()
            )
            dialog.dismiss()
        }
    }

    private fun bleFail(dialog: Dialog) {

        Log.d(TAG, "藍牙連線失敗")
        watchViewModel.setIsBleConnect(false)

        context?.let {
            CoroutineScope(Dispatchers.Main).launch {
                DialogUtil.instance.singleMessageDialog(
                    requireActivity(),
                    "訊息",
                    "連線失敗，請再次嘗試連線",
                    "重試"
                ) {
                    checkGps()
                }
                dialog.dismiss()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        checkGps()
    }

    private fun checkGps() {

        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            DialogUtils().showMultiple(
                requireActivity(),
                "提醒",
                "請開啟定位後才能搜尋到裝置",
                "前往",
                "取消",
                object : DialogUtils.OnMultipleClickListener {
                    override fun onOk() {

                        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                    }

                    override fun onCancel() {

                        onBackPressed()
                    }

                })

        } else {

            if (!WatchUtils.instance.isBluetoothEnabled()) {

                requestBluetooth.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
            } else {

                startScanDevices()
            }
        }
    }

    private var requestBluetooth = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        if (result.resultCode == RESULT_OK) {
            startScanDevices()
        } else {
            onBackPressed()
        }
    }

    private fun startScanDevices() {
        try {

            binding.apply {
                tvNone.visibility = View.GONE
                tvRescan.visibility = View.GONE
                progressbar.visibility = View.VISIBLE
            }
            deviceList.clear()
            watchAdapter.notifyDataSetChanged()

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
            }

            WatchUtils.instance.startScanBle({ code, scanBean ->

                if (code == Constants.CODE.Code_OK) {
                    if (scanBean != null) {
                        binding.progressbar.visibility = View.GONE
                        Log.d(TAG, "找到裝置: ${scanBean.deviceMac}")
                        addDeviceToList(scanBean)
                    }

                } else {

                    if (code == 1) {
                        Log.e(TAG, "搜尋結果: 無資料")
                    } else {
                        Log.e(TAG, "搜尋結果: 逾時")
                    }

                    CoroutineScope(Dispatchers.Main).launch {
                        binding.apply {
                            progressbar.visibility = View.GONE
                            if (deviceList.size == 0) {
                                tvNone.visibility = View.VISIBLE
                                tvRescan.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }, 5)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addDeviceToList(scanBean: ScanDeviceBean) {
        var found = false

        if (deviceList.size > 0) {
            deviceList.forEach { item ->
                if (item.deviceMac.equals(scanBean.deviceMac)) {
                    found = true
                }
            }
        }

        if (!found) {
            deviceList.add(scanBean)
            watchAdapter.notifyItemChanged(deviceList.size - 1)
        }
    }
}