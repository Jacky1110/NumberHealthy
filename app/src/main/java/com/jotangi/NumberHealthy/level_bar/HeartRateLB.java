package com.jotangi.NumberHealthy.level_bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.jotangi.NumberHealthy.R;

import java.util.Locale;

public class HeartRateLB extends View {

    private String TAG = getClass().getSimpleName() + "(TAG)";

    /**
     * 設計要點
     * 1. 數值必需照順序左低右高
     * 2. 至少要兩個值
     * 3. levelValues.length - 1 = barLabel.length, barColor.length
     */
    private final int[] levelValues = {0, 117, 137, 156, 176, 250};
    private final String[] barColor =
            getContext().getResources().getStringArray(R.array.heart_rate_color);
    private final String[] barLabel = {
            "熱身放鬆", "脂肪燃燒", "心肺強化", "耐心強化", "無氧極限"
    };


    private Paint barPaint, textPaint;
    private Path trianglePath;
    private Paint.FontMetrics fontMetrics;


    private int dataValue;
    private int vw, vh;
    private int offsetH;
    private int triangleWidth, triangleHeight, triangleOffsetW;
    private int barWidth, barHeight;

    public HeartRateLB(Context context) {
        super(context);
        init();
    }

    public HeartRateLB(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeartRateLB(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public HeartRateLB(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        dataValue = levelValues[0];

        barPaint = new Paint();
        barPaint.setStyle(Paint.Style.FILL);
        textPaint = new Paint();
        trianglePath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        vw = w;
        vh = h;

        int base = 52;
        offsetH = (h * 2 / base);

        triangleWidth = h * 14 / base;
        triangleHeight = h * 10 / base;
        triangleOffsetW = triangleWidth / 2;

        barWidth = (w - triangleOffsetW * 2) / barColor.length;
        barHeight = h * 20 / base;

        upPosition();

        textPaint.setTextSize(h * 16F / base);
        fontMetrics = textPaint.getFontMetrics();
    }

    private void upPosition() {

        int positionX = calculatePosition();

        trianglePath.reset();
        trianglePath.moveTo(positionX, offsetH);
        trianglePath.lineTo(positionX + triangleWidth, offsetH);
        trianglePath.lineTo(positionX + triangleOffsetW, offsetH + triangleHeight);
        trianglePath.lineTo(positionX, offsetH);
        trianglePath.close();
    }

    private int calculatePosition() {

        if (levelValues.length == 0 || dataValue < levelValues[0]) return 0;

        int size = levelValues.length;
        for (int i = 1; i < size; i++) {

            if (dataValue < levelValues[i]) {

                return (i - 1) * barWidth +
                        (dataValue - levelValues[i - 1]) * barWidth / (levelValues[i] - levelValues[i - 1]);
            }
        }

        return (size - 1) * barWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (vw == 0 || vh == 0 || barColor.length == 0) return;

        // 畫三角形
        barPaint.setColor(Color.RED);
        canvas.drawPath(trianglePath, barPaint);

        // 畫長條圖
        int barY1 = offsetH + triangleHeight + offsetH;
        int barY2 = barY1 + barHeight;
        int barX1 = triangleOffsetW;
        int barX2;
        for (String i : barColor) {
            barX2 = barX1 + barWidth;
            barPaint.setColor(Color.parseColor(i));
            canvas.drawRect(barX1, barY1, barX2, barY2, barPaint);
            barX1 = barX2;
        }

        // 畫數值
        int numY = barY1 + offsetH - Math.round(fontMetrics.ascent);
        barX1 = barWidth + triangleOffsetW;
        textPaint.setColor(Color.WHITE);
        for (int i = 1; i < barLabel.length; i++) {
            String value = String.format(Locale.getDefault(), "%d", levelValues[i]);
            int tw = Math.round(textPaint.measureText(value));
            int numX = barX1 - tw / 2;
            canvas.drawText(value, numX, numY, textPaint);
            barX1 = barX1 + barWidth;
        }

        // 畫說明欄
        textPaint.setColor(Color.BLACK);
        barY1 = barY2 + offsetH - Math.round(fontMetrics.ascent);
        barX1 = triangleOffsetW;
        for (String s : barLabel) {
            int tw = Math.round(textPaint.measureText(s));
            int legendX = barX1;
            int x3 = barX1 + barWidth;
            if (tw < barWidth) {
                legendX += (barWidth - tw) / 2;
            }
            canvas.drawText(s, legendX, barY1, textPaint);
            barX1 = x3;
        }
    }

    public void setDataValue(int v) {

        dataValue = v;
        upPosition();
        invalidate();
    }
}

