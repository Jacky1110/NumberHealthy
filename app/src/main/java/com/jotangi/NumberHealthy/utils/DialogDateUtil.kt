package com.jotangi.NumberHealthy.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.text.InputType
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.jotangi.NumberHealthy.api.watch.*
import com.jotangi.NumberHealthy.databinding.*
import java.util.*

class DialogDateUtil private constructor() {

    private val TAG = "${javaClass.simpleName}(TAG)"

    companion object {
        val instance by lazy { DialogDateUtil() }
    }

    /**
     * 心率
     */
    fun heartRate(activity: Activity, request: (UploadHrRequest) -> Unit) {

        val group = activity.findViewById<ViewGroup>(android.R.id.content)
        val binding = DialogInsertDataBpmBinding.inflate(
            LayoutInflater.from(activity), group, false
        )
        val dialog = AlertDialog.Builder(activity).setView(binding.root).create()

        binding.apply {

            etDate.setText(DateUtil.instance.currentYmdHm())
            etDate.setOnClickListener {
                selectDate(activity, etDate)
            }

            btnInsertValue.setOnClickListener {

                if (etValue.text.toString().isBlank()) {
                    Toast.makeText(activity, "請填妥相關欄位", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                dialog.dismiss()

                request(
                    UploadHrRequest(
                        SharedPreferencesUtil.instances.getAccountId() ?: "",
                        etDate.text.toString(),
                        etValue.text.toString().toInt(),
                        1
                    )
                )
            }
        }

        dialog.show()
    }

    /**
     * 血壓
     */
    fun bloodPressure(activity: Activity, request: (UploadBPRequest) -> Unit) {

        val group = activity.findViewById<ViewGroup>(android.R.id.content)
        val binding = DialogInsertDataBpBinding.inflate(
            LayoutInflater.from(activity), group, false
        )
        val dialog = AlertDialog.Builder(activity).setView(binding.root).create()

        binding.apply {

            etDate.setText(DateUtil.instance.currentYmdHm())
            etDate.setOnClickListener {
                selectDate(activity, etDate)
            }

            btnInsertValue.setOnClickListener {

                if (etSbp.text.toString().isBlank() || etDbp.text.toString().isBlank()) {
                    Toast.makeText(activity, "請填妥相關欄位", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                dialog.dismiss()

                request(
                    UploadBPRequest(
                        SharedPreferencesUtil.instances.getAccountId() ?: "",
                        etDate.text.toString(),
                        etDbp.text.toString().toInt(),
                        etSbp.text.toString().toInt(),
                        1
                    )
                )
            }
        }

        dialog.show()
    }

    /**
     * 血氧
     */
    fun bloodOxygen(activity: Activity, request: (UploadOxygenRequest) -> Unit) {

        val group = activity.findViewById<ViewGroup>(android.R.id.content)
        val binding = DialogInsertDataOxyBinding.inflate(
            LayoutInflater.from(activity), group, false
        )
        val dialog = AlertDialog.Builder(activity).setView(binding.root).create()

        binding.apply {

            etDate.setText(DateUtil.instance.currentYmdHm())
            etDate.setOnClickListener {
                selectDate(activity, etDate)
            }

            btnInsertValue.setOnClickListener {

                if (etValue.text.toString().isBlank()) {
                    Toast.makeText(activity, "請填妥相關欄位", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                dialog.dismiss()

                request(
                    UploadOxygenRequest(
                        SharedPreferencesUtil.instances.getAccountId() ?: "",
                        etDate.text.toString(),
                        etValue.text.toString().toInt(),
                        1
                    )
                )
            }
        }

        dialog.show()
    }

    /**
     * 卡路里
     */
    fun kcal(activity: Activity, request: (UploadKcalRequest) -> Unit) {

        val group = activity.findViewById<ViewGroup>(android.R.id.content)
        val binding = DialogInsertDataKcalBinding.inflate(
            LayoutInflater.from(activity), group, false
        )
        val dialog = AlertDialog.Builder(activity).setView(binding.root).create()

        binding.apply {

            etDate.setText(DateUtil.instance.currentYmdHm())
            etDate.setOnClickListener {
                selectDate(activity, etDate)
            }

            btnInsertValue.setOnClickListener {

                if (etValue.text.toString().isBlank()) {
                    Toast.makeText(activity, "請填妥相關欄位", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                dialog.dismiss()
                request(
                    UploadKcalRequest(
                        SharedPreferencesUtil.instances.getAccountId() ?: "",
                        etDate.text.toString(),
                        etValue.text.toString(),
                        "1",
                    )
                )
            }
        }

        dialog.show()
    }

    /**
     * 雙臂血壓
     */
    fun arm(
        activity: Activity,
        list: List<GetBp2Bean>?,
        request: (UploadArmRequest) -> Unit
    ) {

        val group = activity.findViewById<ViewGroup>(android.R.id.content)
        val binding = DialogInsertDataArmBinding.inflate(
            LayoutInflater.from(activity), group, false
        )
        val dialog = AlertDialog.Builder(activity).setView(binding.root).create()

        binding.apply {

            etDate.setText(DateUtil.instance.currentYmdHm())
            etDate.setOnClickListener {
                selectDate(activity, etDate)
            }

            btnInsertValue.setOnClickListener {

                if (armTrouble(activity, list, binding)) return@setOnClickListener

                dialog.dismiss()
                request(
                    UploadArmRequest(
                        SharedPreferencesUtil.instances.getAccountId() ?: "",
                        etDate.text.toString(),
                        etLDia.text.toString(),
                        etLSys.text.toString(),
                        etLPp.text.toString(),
                        etLMap.text.toString(),
                        etRDia.text.toString(),
                        etRSys.text.toString(),
                        etRPp.text.toString(),
                        etRMap.text.toString(),
                        etHeartRate.text.toString(),
                        "1"
                    )
                )
            }
        }

        dialog.show()
    }

    private fun armTrouble(
        activity: Activity,
        list: List<GetBp2Bean>?,
        binding: DialogInsertDataArmBinding
    ): Boolean {

        binding.apply {

            return when {
                etHeartRate.text.toString().isBlank() ||
                        etLSys.text.toString().isBlank() ||
                        etRSys.text.toString().isBlank() ||
                        etLDia.text.toString().isBlank() ||
                        etRDia.text.toString().isBlank() ||
                        etLPp.text.toString().isBlank() ||
                        etRPp.text.toString().isBlank() ||
                        etLMap.text.toString().isBlank() ||
                        etRMap.text.toString().isBlank() -> {
                    Toast.makeText(activity, "請填妥相關欄位", Toast.LENGTH_SHORT).show()
                    true
                }

                list?.map { it.bloodStartTime }?.contains(
                    DateUtil.instance.fillSecond(etDate.text.toString())
                ) ?: false -> {
                    Toast.makeText(
                        activity, "此時間已重複，請重新選擇時間", Toast.LENGTH_SHORT
                    ).show()
                    true
                }

                else -> false
            }
        }
    }

    /**
     * 黃斑部色素
     */
    fun macularPigment(
        activity: Activity,
        list: List<GetMpodBean>?,
        request: (UploadMpodRequest) -> Unit
    ) {

        val group = activity.findViewById<ViewGroup>(android.R.id.content)
        val binding = DialogInsertDataMpodBinding.inflate(
            LayoutInflater.from(activity), group, false
        )
        val dialog = AlertDialog.Builder(activity).setView(binding.root).create()

        binding.apply {

            etDate.setText(DateUtil.instance.currentYmdHm())
            etDate.setOnClickListener {
                selectDate(activity, etDate)
            }

            etLeftEye.inputType = InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_DECIMAL
            etRightEye.inputType = InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_DECIMAL

            btnInsertValue.setOnClickListener {

                if (macularPigmentTrouble(activity, list, binding))
                    return@setOnClickListener

                dialog.dismiss()
                request(
                    UploadMpodRequest(
                        SharedPreferencesUtil.instances.getAccountId() ?: "",
                        etDate.text.toString(),
                        etLeftEye.text.toString(),
                        etRightEye.text.toString(),
                        "1",
                    )
                )
            }
        }

        dialog.show()
    }

    private fun macularPigmentTrouble(
        activity: Activity,
        list: List<GetMpodBean>?,
        binding: DialogInsertDataMpodBinding
    ): Boolean {

        binding.apply {

            val left = etLeftEye.text.toString()
            val right = etRightEye.text.toString()

            return when {
                left.isBlank() || right.isBlank() -> {
                    Toast.makeText(activity, "請填妥相關欄位", Toast.LENGTH_SHORT).show()
                    true
                }

                left.toFloat() > 1 || right.toFloat() > 1 -> {
                    Toast.makeText(
                        activity, "請輸入正確範圍(0 ~ 1)", Toast.LENGTH_SHORT
                    ).show()
                    true
                }

                list?.map { it.mpodStartTime }?.contains(
                    DateUtil.instance.fillSecond(etDate.text.toString())
                ) ?: false -> {
                    Toast.makeText(
                        activity, "此時間已重複，請重新選擇時間", Toast.LENGTH_SHORT
                    ).show()
                    true
                }

                else -> false
            }
        }
    }

    /**
     * 骨質密度
     */
    fun bone(
        activity: Activity,
        list: List<GetBmdBean>?,
        request: (UploadBmdRequest) -> Unit
    ) {

        val group = activity.findViewById<ViewGroup>(android.R.id.content)
        val binding = DialogInsertDataBmdBinding.inflate(
            LayoutInflater.from(activity), group, false
        )
        val dialog = AlertDialog.Builder(activity).setView(binding.root).create()

        binding.apply {

            etDate.setText(DateUtil.instance.currentYmdHm())
            etDate.setOnClickListener {
                selectDate(activity, etDate)
            }

            etValue.inputType = InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_DECIMAL or
                    InputType.TYPE_NUMBER_FLAG_SIGNED

            btnInsertValue.setOnClickListener {

                if (boneTrouble(activity, list, binding)) return@setOnClickListener

                dialog.dismiss()
                request(
                    UploadBmdRequest(
                        SharedPreferencesUtil.instances.getAccountId() ?: "",
                        etDate.text.toString(),
                        etValue.text.toString(),
                        "1",
                    )
                )
            }
        }

        dialog.show()
    }

    private fun boneTrouble(
        activity: Activity,
        list: List<GetBmdBean>?,
        binding: DialogInsertDataBmdBinding
    ): Boolean {

        binding.apply {

            return when {

                etDate.text.toString().isBlank() -> {
                    Toast.makeText(activity, "請填妥相關欄位", Toast.LENGTH_SHORT).show()
                    true
                }

                etValue.text.toString().toFloat() < -4 ||
                        2 < etValue.text.toString().toFloat() -> {
                    Toast.makeText(
                        activity, "請輸入正確範圍(-4 ~ 2)", Toast.LENGTH_SHORT
                    ).show()
                    true
                }

                list?.map { it.startTime }?.contains(
                    DateUtil.instance.fillSecond(etDate.text.toString())
                ) ?: false -> {
                    Toast.makeText(
                        activity, "此時間已重複，請重新選擇時間", Toast.LENGTH_SHORT
                    ).show()
                    true
                }

                else -> false
            }
        }
    }

    /**
     * 健身鏡
     */
    fun convert(activity: Activity, numValue: (String) -> Unit) {

        val group = activity.findViewById<ViewGroup>(android.R.id.content)
        val binding = DialogConvertBinding.inflate(
            LayoutInflater.from(activity), group, false
        )

        val dialog = AlertDialog.Builder(activity).setView(binding.root).create()
        binding.apply {

            ivAdd.setOnClickListener {

                tvValue.text = (tvValue.text.toString().toInt() + 1).toString()
            }

            ivMinus.setOnClickListener {

                val value = tvValue.text.toString().toInt()
                if (value < 1) {
                    tvValue.text = "0"
                } else {
                    tvValue.text = (value - 1).toString()
                }
            }

            tvCancel.setOnClickListener {
                dialog.dismiss()
            }

            tvOk.setOnClickListener {
                dialog.dismiss()
                numValue(tvValue.text.toString())
            }
        }

        dialog.show()
    }

    /**
     * 選取時間
     */
    private fun selectDate(activity: Activity, etTime: EditText) {

        var dateVale: String

        val calendar: Calendar =
            DateUtil.instance.ymdhmToCalendar(etTime.text.toString())

        DatePickerDialog(
            activity,
            { p0, p1, p2, p3 ->

                dateVale = "${p1}-${
                    DateUtil.instance.fixTimeLength((p2 + 1).toString())
                }-${
                    DateUtil.instance.fixTimeLength(p3.toString())
                } "
                Log.d(TAG, "dateVale: $dateVale")

                TimePickerDialog(
                    activity,
                    { timePicker, i, i2 ->

                        dateVale += "${
                            DateUtil.instance.fixTimeLength(i.toString())
                        }:${
                            DateUtil.instance.fixTimeLength(i2.toString())
                        }"
                        Log.d(TAG, "dateTimeVale: $dateVale")

                        etTime.setText(dateVale)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(activity)
                ).show()

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}