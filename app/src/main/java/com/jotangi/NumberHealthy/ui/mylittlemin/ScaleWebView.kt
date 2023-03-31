package com.jotangi.NumberHealthy.ui.mylittlemin;

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.webkit.WebView
import kotlin.math.sqrt

/**
 * -----------------------------------------------------------------
 * Copyright (C) 2012-2021 by Hermit Lin. All rights reserved.
 * -----------------------------------------------------------------
 *
 * File: ScaleWebView.kt
 * Author: Hermit Lin (hermitnull@gmail.com)
 * Version: V0.0.2 2021/8/24
 * Create: 2021/8/21 上午 11:52
 *
 * -----------------------------------------------------------------
 * Release note:
 * V0.0.1 2021/8/24 (Hermit Lin)
 * 1.Create ScaleWebView.kt
 * -----------------------------------------------------------------
 * Description:
 *
 * 加入了放大縮小的WebView
 * 但在頁面上有其他UI的情形下，放大縮小會顯得突兀。
 * 其主要原因是使用了ScaleX, Y的方法，會使其繪圖區域超過其Layout spec
 *
 */
class ScaleWebView : WebView {
    private val TAG = "ScaleWebView"
    private var isEventStart = false
    private var firstDistance = 0.0

    override fun onTouchEvent(event: MotionEvent): Boolean {

        return when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                Log.w(TAG, "ACTION_DOWN:${event.getX(0)}, ${event.getY(0)}")
                super.onTouchEvent(event)
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount >= 2) {
                    isEventStart = true
                    firstDistance = get2PointDistance(event)
                    return true
                }
                super.onTouchEvent(event)
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.pointerCount >= 2 && isEventStart) {
                    var scrollRatio = scaleX
                    //依照基礎距離放大
                    val distance = get2PointDistance(event)
                    scrollRatio = (scrollRatio * distance / firstDistance).toFloat()
                    if (scrollRatio > 4f) scrollRatio = 4f
                    if (scrollRatio < 1f) scrollRatio = 1f
                    pivotX = getCenterX(event)
                    pivotY = getCenterY(event)
                    Log.w(TAG, "center:${pivotX}, ${pivotY}")
                    scaleX = scrollRatio
                    scaleY = scrollRatio
                    return true
                }
                super.onTouchEvent(event)
            }
            MotionEvent.ACTION_UP -> {
                super.onTouchEvent(event)
            }
            MotionEvent.ACTION_POINTER_UP -> {
                if (isEventStart) {
                    isEventStart = false
                    return true
                }
                super.onTouchEvent(event)
            }
            else -> {
                super.onTouchEvent(event)
            }
        }

    }


    private fun get2PointDistance(event: MotionEvent): Double {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt((x * x + y * y).toDouble())
    }

    private fun getCenterX(event: MotionEvent): Float {
        return (event.getX(0) + event.getX(1)) / 2f
    }

    private fun getCenterY(event: MotionEvent): Float {
        return (event.getY(0) + event.getY(1)) / 2f
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )
}