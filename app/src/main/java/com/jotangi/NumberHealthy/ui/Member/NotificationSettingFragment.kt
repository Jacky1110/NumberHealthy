package com.jotangi.NumberHealthy.ui.Member

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jotangi.NumberHealthy.BaseFragment
import com.jotangi.NumberHealthy.databinding.FragmentNotificationSettingBinding
import com.jotangi.NumberHealthy.databinding.ToolbarBinding
import com.jotangi.NumberHealthy.utils.SharedPreferencesUtil

class NotificationSettingFragment : BaseFragment() {

    private lateinit var binding: FragmentNotificationSettingBinding
    override fun getToolBar(): ToolbarBinding = binding.toolbar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbarArrow("提醒通知")

        binding.apply {
            switchPhone.apply {
                isChecked = getStatus("通話")
                setOnClickListener {
                    setStatus("通話", isChecked)
                    setStatus("未接電話", isChecked)
                    setStatus("來電", isChecked)
                    setStatus("未接來電", isChecked)
                }
            }
            switchMsg.apply {
                var key = "messaging"
                isChecked = getStatus(key)
                setOnClickListener {
                    setStatus(key, isChecked)
                }
            }
            switchMail.apply {
                var key = "gm"
                isChecked = getStatus(key)
                setOnClickListener {
                    setStatus(key, isChecked)
                }
            }
            switchCalendar.apply {
                val key = "calendar"
                isChecked = getStatus(key)
                setOnClickListener {
                    setStatus(key, isChecked)
                }
            }
            switchWeChat.apply {
                var key = "wechat"
                isChecked = getStatus(key)
                setOnClickListener {
                    setStatus(key, isChecked)
                }
            }
            switchQQ.apply {
                var key = "qq"
                isChecked = getStatus(key)
                setOnClickListener {
                    setStatus(key, isChecked)
                }
            }
            switchWeibo.apply {
                var key = "weibo"
                isChecked = getStatus(key)
                setOnClickListener {
                    setStatus(key, isChecked)
                }
            }
            switchFB.apply {
                var key = "facebook"
                isChecked = getStatus(key)
                setOnClickListener {
                    setStatus(key, isChecked)
                }
            }
            switchTwitter.apply {
                var key = "twitter"
                isChecked = getStatus(key)
                setOnClickListener {
                    setStatus(key, isChecked)
                }
            }
            switchMessenger.apply {
                var key = "messenger"
                isChecked = getStatus(key)
                setOnClickListener {
                    setStatus(key, isChecked)
                }
            }
            switchWhatsAPP.apply {
                var key = "whatsapp"
                isChecked = getStatus(key)
                setOnClickListener {
                    setStatus(key, isChecked)
                }
            }
            switchLinkedIn.apply {
                var key = "linkedin"
                isChecked = getStatus(key)
                setOnClickListener {
                    setStatus(key, isChecked)
                }
            }
            switchInstagram.apply {
                var key = "instagram"
                isChecked = getStatus(key)
                setOnClickListener {
                    setStatus(key, isChecked)
                }
            }
            switchSkype.apply {
                var key = "skype"
                isChecked = getStatus(key)
                setOnClickListener {
                    setStatus(key, isChecked)
                }
            }
            switchLine.apply {
                var key = "line"
                isChecked = getStatus(key)
                setOnClickListener {
                    setStatus(key, isChecked)
                }
            }
            switchSnapchat.apply {
                var key = "snapchat"
                isChecked = getStatus(key)
                setOnClickListener {
                    setStatus(key, isChecked)
                }
            }
            switchAppNotif.apply {
                var key = "appnotif"
                isChecked = getStatus(key)
                setOnClickListener {
                    setStatus(key, isChecked)
                }
            }
        }
    }

    private fun getStatus(key: String): Boolean {
        return SharedPreferencesUtil.instances.getNotifyStatus(key)
    }

    private fun setStatus(key: String, value: Boolean) {
        SharedPreferencesUtil.instances.setNotifyStatus(key, value)
    }
}