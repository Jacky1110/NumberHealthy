package com.jotangi.NumberHealthy.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class BatteryView extends View {

    private int mPower = 100;       // 電池電量
    private int width;              // battery width
    private int height;             // battery total height
    private int margin = 2;         //電池內心與border distance
    private int border = 2;         //  battery border line width
    private float radius = 4;       // round coner angel
    private boolean isCharge = false;
    private int chargePower;
    private Timer mTimer;

    public BatteryView(Context context) {
        super(context);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setAntiAlias(true);// 去鋸齒
        paint.setColor(Color.BLACK);// setting paintting color
        float headWidth = width / 20.0f;// 電池頭寬度

        // paitting border
        paint.setStyle(Paint.Style.STROKE);// 空心矩形
        paint.setStrokeWidth(border);
        RectF rect_1 = new RectF(border / 2, border / 2, width - headWidth - border / 2, height - border / 2);
        canvas.drawRoundRect(rect_1, radius, radius, paint);

        // paintting battery head
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(0);
        RectF rect_2 = new RectF(width - headWidth - border / 2, height * 0.25f, width, height * 0.75f);
        canvas.drawRect(rect_2, paint);

        // paitting battery charge
        if (isCharge) {
            paint.setColor(Color.GREEN);
        } else {
            if (mPower < 20) {
                paint.setColor(Color.RED);
            } else if (mPower >= 20 && mPower < 60) {
                paint.setColor(Color.YELLOW);
            } else {
                paint.setColor(Color.GREEN);
            }
        }
        float offset = (width - headWidth - border - margin) * mPower / 100.f;
        RectF rect_3 = new RectF(border + margin, border + margin, offset, height - border - margin);
        canvas.drawRoundRect(rect_3, radius, radius, paint);

    }

    /**
     * setting battery charge
     *
     * @param mPower battery percent 1-100
     */
    public void setPower(@IntRange(from = 1, to = 100) int mPower) {
        this.mPower = mPower;
        if (this.mPower < 0) {
            this.mPower = 0;
        }
        if (this.mPower > 100) {
            this.mPower = 100;
        }
        postInvalidate();
    }

    /**
     * get battery charge
     *
     * @return battery charge percent  1-100
     */
    public int getPower() {
        return mPower;
    }

    /**
     * 設置充電模式 if==true open thread 使battery 無限循環   營造出一個正在充電的狀態
     *
     * @param charge
     */
    public void setCharge(boolean charge) {
        isCharge = charge;
        if (null != mTimer) {
            mTimer.cancel();
            mTimer = null;
        }
        if (isCharge) {
            chargePower = 0;
            mTimer = new Timer();
            mTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    chargePower += 10;
                    if (chargePower > 100) {
                        chargePower = 0;
                    }
                    setPower(chargePower);
                }
            }, 500, 300);
        }
    }
}

