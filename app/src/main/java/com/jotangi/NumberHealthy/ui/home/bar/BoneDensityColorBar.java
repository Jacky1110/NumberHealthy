package com.jotangi.NumberHealthy.ui.home.bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Locale;

public class BoneDensityColorBar extends View {

    private int[] barColor = {
            Color.rgb(242, 120, 109),
            Color.rgb(255, 224, 97),
            Color.rgb(176, 208, 148),
    };

    private String[] barLabel = {
            "疏鬆",
            "流失",
            "正常"
    };

    // 設定相關數值時, levelValues必須比barLabel, barColor多一個, 畫圖時邏輯才不會出錯
    private int[] levelValues = {0, 150, 300, 600};

    private int labelColor = Color.BLACK;
    private int valueColor = Color.WHITE;
    private int triangleColor = Color.rgb(254, 104, 103);

    private int vw;
    private int vh;
    private int triangleHeight;
    private int triangleWidth;
    private int triangleLeft;
    private int barHeight;
    private int barWidth;
    private float labelSize;
    private int offsetY;

    private int dataValue;

    private Paint.FontMetrics fontMetrics;
    private Path trianglePath;
    private Paint barPaint;
    private Paint textPaint;

    public BoneDensityColorBar(Context context) {
        super(context);
        init();
    }

    public BoneDensityColorBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BoneDensityColorBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BoneDensityColorBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        dataValue = levelValues[1];
        barPaint = new Paint();
        barPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setTextSize(labelSize);

        triangleLeft = calculateTriangleLeft();
        trianglePath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        vw = w;
        vh = h;
        offsetY = (h * 2 / 50);
        triangleHeight = h * 6 / 50;
        triangleWidth = h * 12 / 50;
        barHeight = h * 20 / 50;
        barWidth = w / barColor.length;
        labelSize = h * 16 / 50f;
        updateTrianglePosition();
        textPaint.setTextSize(labelSize);
        fontMetrics = textPaint.getFontMetrics();
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
        barPaint.setColor(triangleColor);
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

        // draw number on color bar
        int y1 = y + offsetY - Math.round(fontMetrics.ascent);
        x = barWidth;
        textPaint.setColor(valueColor);
        String[] sList = {"-4", "-2.5", "-1", "2"};
        for (int i = 1; i < barLabel.length; i++) {
            String v = String.format(Locale.getDefault(), "%s", sList[i]);
            int tw = Math.round(textPaint.measureText(v));
            int x1 = x - tw / 2;
            canvas.drawText(v, x1, y1, textPaint);
            x = x + barWidth;
        }

        // draw bottom label
        textPaint.setColor(labelColor);
        y = y2 + offsetY - Math.round(fontMetrics.ascent);
        x = 0;
        for (int i = 0; i < barLabel.length; i++) {
            int tw = Math.round(textPaint.measureText(barLabel[i]));
            int x1 = x;
            int x2 = x + barWidth;
            if (tw < barWidth) {
                x1 += (barWidth - tw) / 2;
            }
            canvas.drawText(barLabel[i], x1, y, textPaint);
            x = x2;
        }
    }

    public void setDataValue(String value) {

        float[] realValues = {-4, -2.5f, -1, 2};
        int num;
        try {
            float f = Float.parseFloat(value);
            if (f < -4) {
                f = -4;
            }
            if (f > 2) {
                f = 2;
            }

            f += 4;
            num = (int) (f * 100);

            if (num > 586) {
                num = 586;
            }
        } catch (Exception e) {
            e.printStackTrace();
            num = 599;
        }

        dataValue = num;
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


