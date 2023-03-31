package com.jotangi.NumberHealthy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.jotangi.NumberHealthy.R;


public class CircleHoleView extends View {
    private final String TAG = this.getClass().getSimpleName();

    private Bitmap bitmap = null;
    private Paint p2;

    public CircleHoleView(Context context) {
        super(context);
        init();
    }

    public CircleHoleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleHoleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CircleHoleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        p2 = new Paint();
        p2.setAlpha(0);
        p2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        p2.setAntiAlias(true);
        initBitmap();
    }

    private void initBitmap() {
        int w = getWidth();
        int h = getHeight();
        if (w > 0 && h > 0) {
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas pc = new Canvas(bitmap);
            pc.drawColor(getResources().getColor(R.color.gray_mask, null));
            pc.drawCircle(w / 2, h / 2, w / 2 * 3, p2);
//            pc.drawRoundRect(new RectF(100,100,100,100), 60, 60, p2);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout(), changed=" + changed + ", left=" + left + ",right=" + right);
        if (changed && (right - left) > 0 && (bottom - top) > 0) {
            initBitmap();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0, 0, null);
        }
    }
}
