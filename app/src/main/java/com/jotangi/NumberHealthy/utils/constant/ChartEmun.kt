package com.jotangi.NumberHealthy.utils.constant

/**
 * LineChart
 */
enum class LineCommon(val value: Float) {
    LineWith(2F),
    HourLow(0F), HourHigh(10F),
    XLow(-.5F), XHigh(6.5F),
}

enum class LineInit(val value: Int) {
    Error(-123), InitMax(0), InitMin(1200),
}

/**
 * BarChart
 */
enum class ChartTextSize(val value: Float) {
    AxisX(12F), AxisR(14F), Cost(14F)
}

enum class BcWith(val value: Float) {
    Size(0.5F)
}

enum class InitValue(val value: Float) {
    Entries(-111F)
}

enum class TopSpace(val value: Float) {
    Min(1.2F), High(1.8F), Half(1.5F)
}

enum class LimitValue(val value: Float) {
    YLow(60F), YHigh(160F)
}


/**
 * 呼吸率
 */
enum class BreathRateColor(val value: String) {
    Theme("#FFB800")
}

/**
 * 體溫
 */
enum class BodyTemperatureColor(val value: String) {
    Theme("#CC6600")
}

enum class RangeBodyTem(val value: Float) {
    Mini(34F), Max(41F)
}


/**
 * 睡眠
 */
enum class SleepColor(val value: String) {
    Deep("#6A50B5"), Total("#D8D0F2")
}

/**
 * 步數紀錄
 */
enum class StepColor(val value: String) {
    Theme("#38D6FD")
}

enum class RangeStep(val value: Float) {
    YMax(1000F), YMin(0F)
}

/**
 * 卡路里
 */
enum class KcalColor(val value: String) {
    Theme("#3F51B5")
}

/**
 * 黃斑部色素
 */
enum class MacularColor(val value: String) {
    Left("#6A50B5"), Right("#FE8E7F")
}

enum class MacularElement(val value: Float) {
    YMax(1F), YMin(0F), Error(-37F)
}

/**
 * 骨密範圍
 */
enum class BoneColor(val value: String) {
    Red("#EC6761"),
    Yellow("#FCEF9B"),
    Green("#B0D094")
}

enum class RangeBone(val value: Float) {
    Mini(0F), Max(6F)
}