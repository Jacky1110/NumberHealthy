package com.jotangi.NumberHealthy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jotangi.NumberHealthy.R;

import java.util.Locale;

public class MaskView extends ConstraintLayout {
    private final String TAG = this.getClass().getSimpleName();

    private boolean isTouching = false;
    // 手指碰觸螢幕的啟始位置
    private int mStartX;
    private int mStartY;
    private int mOffsetX;
    private int mOffsetY;
    private View vwHole;
    private View vwTop;
    private View vwBottom;
    private View vwLeft;
    private View vwRight;
    private ImageView vwImage;

    private ConstraintLayout rootView;

    public MaskView(Context context) {
        super(context);
        init(null, 0);
    }

    public MaskView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public MaskView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, 0);
    }

    public MaskView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyle) {
        rootView = (ConstraintLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_mask, this);
        vwHole = rootView.findViewById(R.id.vw_hole);
        vwTop = rootView.findViewById(R.id.vw_top_mask);
        vwBottom = rootView.findViewById(R.id.vw_bottom_mask);
        vwLeft = rootView.findViewById(R.id.vw_left_mask);
        vwRight = rootView.findViewById(R.id.vw_right_mask);
        vwImage = findViewById(R.id.iv_image);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                processActionDown(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                processActionMove(x, y);
                break;
            case MotionEvent.ACTION_UP:
                processActionUp(x, y);
                break;
            default:
                return false;
        }

        return true;

    }

    private void processActionDown(int x, int y) {
        mStartX = x;
        mStartY = y;

        Rect r = new Rect(vwHole.getLeft(), vwHole.getTop(), vwHole.getRight(), vwHole.getBottom());
        String log = String.format(Locale.getDefault(), "processActionDown(), x=%d, y=%d, hole.top=%d, hole.left=%d, hole.bottom=%d, hole.right=%d",
                x, y, vwHole.getTop(), vwHole.getLeft(), vwHole.getBottom(), vwHole.getRight());

        if (r.contains(x, y)) {
            mOffsetX = x - r.left;
            mOffsetY = y - r.top;
            isTouching = true;
        }

    }

    private void processActionMove(int x, int y) {
        if (isTouching) {
            int newLeft = x - mOffsetX;
            int newTop = y - mOffsetY;
            if (newLeft <= 0) {
                newLeft = 1;
            }
            if (newTop <= 0) {
                newTop = 1;
            }

            if ((newLeft + vwHole.getWidth()) > getWidth()) {
                newLeft = getWidth() - vwHole.getWidth();
            }
            if ((newTop + vwHole.getHeight()) > getHeight()) {
                newTop = getHeight() - vwHole.getHeight();
            }

            LayoutParams lptop = (LayoutParams) vwTop.getLayoutParams();
            lptop.height = newTop;
            vwTop.setLayoutParams(lptop);

            LayoutParams lpleft = (LayoutParams) vwLeft.getLayoutParams();
            lpleft.width = newLeft;
            vwLeft.setLayoutParams(lpleft);
        }
    }

    private void processActionUp(int x, int y) {
        isTouching = false;
    }

    public void setImageBitmap(Bitmap bitmap) {
        vwImage.setImageBitmap(bitmap);
    }

    public Bitmap cropImage() {
        //setDrawingCacheEnabled -->it gets all view cache picture
        vwImage.setDrawingCacheEnabled(true);
        //buildDrawingCache-->建立相對應緩存照片
        vwImage.buildDrawingCache(true);
        Bitmap bitmap1 = vwImage.getDrawingCache();
        Bitmap bitmap2 = Bitmap.createBitmap(bitmap1, vwHole.getLeft(), vwHole.getTop(), vwHole.getWidth(), vwHole.getHeight());
        vwImage.setDrawingCacheEnabled(false);
        destroyDrawingCache();
        return bitmap2;
    }
}
