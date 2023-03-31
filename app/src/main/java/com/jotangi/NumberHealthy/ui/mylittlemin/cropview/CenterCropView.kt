package com.jotangi.NumberHealthy.ui.mylittlemin.cropview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.jotangi.NumberHealthy.R
import java.util.*

class CenterCropView : ConstraintLayout {
    private val TAG = this.javaClass.simpleName
    private var isTouching = false

    // 手指碰觸螢幕的啟始位置
    private var mStartX = 0
    private var mStartY = 0
    private var mOffsetX = 0
    private var mOffsetY = 0
    private var vwHole: View? = null
    private var vwTop: View? = null
    private var vwBottom: View? = null
    private var vwLeft: View? = null
    private var vwRight: View? = null
    private var vwImage: ImageView? = null
    private var rootView: ConstraintLayout? = null

    constructor(context: Context?) : super(context!!) {
        init(null, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init(attrs, 0)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context!!, attrs, defStyleAttr
    ) {
        init(attrs, 0)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(
        context!!, attrs, defStyleAttr, defStyleRes
    ) {
        init(attrs, defStyleRes)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        rootView =
            LayoutInflater.from(context)
                .inflate(R.layout.register_camera_crop_view, this) as ConstraintLayout?
        vwHole = rootView!!.findViewById(R.id.vw_hole)
        vwTop = rootView!!.findViewById(R.id.vw_top_mask)
        vwBottom = rootView!!.findViewById(R.id.vw_bottom_mask)
        vwLeft = rootView!!.findViewById(R.id.vw_left_mask)
        vwRight = rootView!!.findViewById(R.id.vw_right_mask)
        vwImage = findViewById(R.id.iv_image)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.action) {
            MotionEvent.ACTION_DOWN -> processActionDown(x, y)
            MotionEvent.ACTION_MOVE -> processActionMove(x, y)
            MotionEvent.ACTION_UP -> processActionUp(x, y)
            else -> return false
        }
        /*如果需要change centerview location 改成return true*/
        // return false
        return true
    }

    private fun processActionDown(x: Int, y: Int) {
        mStartX = x
        mStartY = y
        val r = Rect(vwHole!!.left, vwHole!!.top, vwHole!!.right, vwHole!!.bottom)
        val log = String.format(
            Locale.getDefault(),
            "processActionDown(), x=%d, y=%d, hole.top=%d, hole.left=%d, hole.bottom=%d, hole.right=%d",
            x,
            y,
            vwHole!!.top,
            vwHole!!.left,
            vwHole!!.bottom,
            vwHole!!.right
        )
        Log.d(TAG, log)
        if (r.contains(x, y)) {
            mOffsetX = x - r.left
            mOffsetY = y - r.top
            isTouching = true
        }
    }

    private fun processActionMove(x: Int, y: Int) {
        Log.d(TAG, "processActionMove(), x=$x,y=$y,isTouching=$isTouching")
        if (isTouching) {
            var newLeft = x - mOffsetX
            var newTop = y - mOffsetY
            if (newLeft <= 0) {
                newLeft = 1
            }
            if (newTop <= 0) {
                newTop = 1
            }
            if (newLeft + vwHole!!.width > width) {
                newLeft = width - vwHole!!.width
            }
            if (newTop + vwHole!!.height > height) {
                newTop = height - vwHole!!.height
            }
            val lptop = vwTop!!.layoutParams as LayoutParams
            lptop.height = newTop
            vwTop!!.layoutParams = lptop
            val lpleft = vwLeft!!.layoutParams as LayoutParams
            lpleft.width = newLeft
            vwLeft!!.layoutParams = lpleft
        }
    }

    private fun processActionUp(x: Int, y: Int) {
        isTouching = false
    }

    fun setImageBitmap(bitmap: Bitmap?) {
        vwImage!!.setImageBitmap(bitmap)
    }

    fun cropImage(): Bitmap {
        vwImage!!.isDrawingCacheEnabled = true
        vwImage!!.buildDrawingCache(true)
        val bitmap1 = vwImage!!.drawingCache

        val bitmap2 = Bitmap.createBitmap(
            bitmap1,
            vwHole!!.left,
            vwHole!!.top,
            vwHole!!.width,
            vwHole!!.height
        )
        vwImage!!.isDrawingCacheEnabled = false
        destroyDrawingCache()
        return bitmap2
    }
}
