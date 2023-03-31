package com.jotangi.NumberHealthy.ui.home.watch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.databinding.FragmentWatchNotificationSettingBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding

class WatchNotificationSettingFragment : BaseFragment() {
    private lateinit var _binding: FragmentWatchNotificationSettingBinding
    private val binding get() = _binding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWatchNotificationSettingBinding.inflate(inflater, container, false)

        init()
//        initAction()

        return binding.root
    }

    private fun init() {

//        YCBTClient.deviceToApp(new BleDeviceToAppDataResponse() {
//            @Override
//            public void onDataResponse(int i, HashMap hashMap) {
//                if (hashMap != null) {
//                    if (i == 0) {//
//                        int dataType = (int) hashMap.get("dataType");
//                        int data = -1;
//                        if (hashMap.get("data") != null)
//                            data = (int) hashMap.get("data");
//                        switch (dataType) {
//                            case Constants.DATATYPE.AppECGPPGStatus:
//                            int EcgStatus = (int) hashMap.get("EcgStatus");
//                            int PPGStatus = (int) hashMap.get("PPGStatus");
//                            if (PPGStatus == 0) {
//
//                            }
//                            break;
//                            case Constants.DATATYPE.DeviceFindMobile:
//                            if (data == 0) {
//
//                            }
//                            break;
//                            case Constants.DATATYPE.DeviceLostReminder:
//                            if (data == 0) {
//
//                            }
//                            break;
//                            case Constants.DATATYPE.DeviceAnswerAndClosePhone:
//                            if (data == 0) {
//
//                            }
//                            break;
//                            case Constants.DATATYPE.DeviceTakePhoto:
//                            if (data == 0) {
//
//                            }
//                            break;
//                            case Constants.DATATYPE.DeviceStartMusic:
//                            if (data == 0) {
//
//                            }
//                            break;
//                            case Constants.DATATYPE.DeviceSos:
//
//                            break;
//                            case Constants.DATATYPE.DeviceDrinkingPatterns:
//
//                            break;
//                        }
//                    }
//                }
//            }
//        });
    }
}