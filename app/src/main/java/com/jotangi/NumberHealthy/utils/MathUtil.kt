package com.jotangi.NumberHealthy.utils

import com.jotangi.NumberHealthy.utils.constant.LineInit
import java.math.RoundingMode
import java.text.DecimalFormat

class MathUtil private constructor() {

    private val TAG = "${javaClass.simpleName}(TAG)"

    companion object {
        val instance by lazy { MathUtil() }
    }

    fun fixUpToInt(f: Float): Int {
        return try {
            DecimalFormat("#").run {
                roundingMode = RoundingMode.UP
                format(f)
            }.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            LineInit.Error.value
        }
    }

    fun fixDownToInt(f: Float): Int {
        return try {
            DecimalFormat("#").run {
                roundingMode = RoundingMode.DOWN
                format(f)
            }.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            LineInit.Error.value
        }
    }

    /**
     * 睡眠
     */
    fun fixSleepValue(value: Float): String {
        return try {
            DecimalFormat("#.#").run {
                roundingMode = RoundingMode.DOWN
                format(value)
            }
        }catch (e: Exception) {
            e.printStackTrace()
            "-"
        }
    }

    /**
     * 黃斑部
     */
    fun fixMacularValue(value: Float): String {
        return try {
            DecimalFormat("#.##").run {
                roundingMode = RoundingMode.DOWN
                format(value)
            }
        }catch (e: Exception) {
            e.printStackTrace()
            "-"
        }
    }

    /**
     * 骨質密度
     */
    fun fixBoneValue(value: String?): String {
        return try {
            DecimalFormat("#.##").run {
                roundingMode = RoundingMode.DOWN
                format(value?.toFloat() ?: return "")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}