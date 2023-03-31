package com.jotangi.NumberHealthy.ui.home.bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class SleepColorBar extends View {
    private int[] barColor = {
            Color.rgb(77, 77, 255),
            Color.rgb(61, 24, 180),
            Color.rgb(77, 77, 255),
            Color.rgb(61, 24, 180),
            Color.rgb(77, 77, 255),
            Color.rgb(61, 24, 180),

    };

    // 設定相關數值時, levelValues必須比barLabel, barColor多一個, 畫圖時邏輯才不會出錯
    private int[] levelValues = {0, 117, 137, 156, 176, 250};

    private int triangleColor = Color.rgb(255, 255, 255);
    private int triangleColor2 = Color.argb(0, 255, 255, 255);
    private int vw;
    private int vh;
    private int triangleHeight;
    private int triangleWidth;
    private int triangleLeft;
    private int barHeight;
    private int barWidth;
    private int offsetY;

    private int dataValue;

    private Path trianglePath;
    private Paint barPaint;

    public SleepColorBar(Context context) {
        super(context);
        init();
    }

    public SleepColorBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SleepColorBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SleepColorBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        dataValue = levelValues[1];
        barPaint = new Paint();
        barPaint.setStyle(Paint.Style.FILL);

        triangleLeft = calculateTriangleLeft();
        trianglePath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        vw = w;
        vh = h;
        offsetY = (h * 2 / 50);
        triangleHeight = h * 12 / 50;
        triangleWidth = h * 24 / 50;
        barHeight = h * 20;
        barWidth = w / barColor.length;
        updateTrianglePosition();
    }

    private int calculateTriangleLeft() {

        if (barColor.length < 1) return 0;

        int index = 0;
        while (index < barColor.length) {
            if (dataValue >= levelValues[index] && dataValue < levelValues[index + 1]) {
                return index * barWidth + (dataValue - levelValues[index]) * barWidth / (levelValues[index + 1] - levelValues[index]);
            }
            index++;
        }
        return 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (vw == 0 || vh == 0) return;
        if (barColor.length == 0) return;

        // draw triangle
        barPaint.setColor(triangleColor2);
        canvas.drawPath(trianglePath, barPaint);

        // draw color bar
        int y = offsetY + triangleHeight + offsetY;
        int x = 0;
        int y2 = y + barHeight;
        for (int i = 0; i < barColor.length; i++) {
            int x2 = x + barWidth;
            barPaint.setColor(barColor[i]);
            canvas.drawRect(x, y, x2, y2, barPaint);
            x = x2;
        }
    }

    public void setDataValue(int v) {
        dataValue = v;
        updateTrianglePosition();
        invalidate();
    }

    private void updateTrianglePosition() {
        triangleLeft = calculateTriangleLeft();
        trianglePath.reset();
        trianglePath.moveTo(triangleLeft, offsetY);
        trianglePath.lineTo(triangleLeft + triangleWidth, offsetY);
        trianglePath.lineTo(triangleLeft + triangleWidth / 2, offsetY + triangleHeight);
        trianglePath.lineTo(triangleLeft, offsetY);
        trianglePath.close();
    }
}
