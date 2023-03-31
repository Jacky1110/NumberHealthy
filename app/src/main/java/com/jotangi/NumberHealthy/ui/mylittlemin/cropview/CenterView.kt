package com.jotangi.NumberHealthy.ui.mylittlemin.cropview

import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.jotangi.NumberHealthy.R

class CenterView : View {
    private val TAG = this.javaClass.simpleName
    private lateinit var bitmap: Bitmap
    private var p2: Paint? = null

    @RequiresApi(Build.VERSION_CODES.M)
    constructor(context: Context?) : super(context) {
        init()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun init() {
        p2 = Paint()
        p2!!.alpha = 0
        p2!!.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        p2!!.isAntiAlias = true
        initBitmap()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun initBitmap() {
        val w = width
        val h = height
        if (w > 0 && h > 0) {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val pc = Canvas(bitmap)
            pc.drawColor(resources.getColor(R.color.gray_mask, null))
            pc.drawRect(1.toFloat(), 1.toFloat(), w.toFloat(), h.toFloat(), p2!!)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d(TAG, "onLayout(), changed=$changed, left=$left,right=$right")
        if (changed && right - left > 0 && bottom - top > 0) {
            initBitmap()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (bitmap != null) {
            canvas.drawBitmap(bitmap!!, 0f, 0f, null)
        }

    }
}
