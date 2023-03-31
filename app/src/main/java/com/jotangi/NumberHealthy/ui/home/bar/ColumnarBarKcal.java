package com.jotangi.NumberHealthy.ui.home.bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.List;
import java.util.Locale;

/**
 * top 16dp, left 16dp right 40dp bottom 16dp
 * 垂直間隔 2
 * 色條高 20
 * 垂直間隔 2
 * 文字高 16
 * 下間隔 2
 * 色條平均分配View的寬度
 */
public class ColumnarBarKcal extends View {

    private int[] dataValue = {0, 0, 0, 0, 0, 0, 0};

    // 設定相關數值時, levelValues必須比barLabel, barColor多一個, 畫圖時邏輯才不會出錯
    private int[] axisYValue = {0, 200, 400, 600, 800, 1000};
    private String[] axisXLable = {"", "", "", "", "", "", ""};
    private double[] axisYDoubleValue = new double[0];
    private int[] levelValues = {200, 400, 600, 800, 1000};

    private final int[] barColor = {
            Color.rgb(220, 227, 241),
            Color.rgb(220, 227, 241),
            Color.rgb(220, 227, 241),
            Color.rgb(220, 227, 241)
    };

    private final int lineColor = Color.rgb(0xE0, 0xE0, 0xE0);

    private int topm;
    private int leftm;
    private int rightm;
    private int bottomm;

    private int vw;
    private int vh;
    private int barWidth;
    private float labelSize;
    private int offsetX;
    private int offsetY;
    private int strokeWidth = 3;

    private Rect chartRect = new Rect(0, 0, 0, 0);

    private Paint.FontMetrics fontMetrics;
    private Paint barPaint;
    private Paint textPaint;

    public ColumnarBarKcal(Context context) {
        super(context);
        init();
    }

    public ColumnarBarKcal(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColumnarBarKcal(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ColumnarBarKcal(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        barPaint = new Paint();
        barPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setTextSize(labelSize);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        topm = Math.round(16 * displayMetrics.density);
        bottomm = Math.round(40 * displayMetrics.density);
        leftm = Math.round(16 * displayMetrics.density);
        rightm = Math.round(50 * displayMetrics.density);

        strokeWidth = Math.round(2 * displayMetrics.density);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        vw = w;
        vh = h;

        if (w != 0 && h != 0) {
            chartRect = new Rect(leftm, topm, w - rightm, h - bottomm);
            offsetX = (chartRect.width() / (dataValue.length + 1));
            barWidth = (chartRect.width() / (dataValue.length * 2 + 1));
            updateTypeValues();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            if (vw == 0 || vh == 0) return;
            if (barColor.length == 0) return;

            // draw chart rect
            barPaint.setColor(lineColor);
            barPaint.setStyle(Paint.Style.STROKE);
            barPaint.setStrokeWidth(strokeWidth);
            canvas.drawRect(chartRect, barPaint);

            // draw horizontal line in chart
            int y = topm + offsetY;
            int co = 0;

            if (axisYValue.length == 0) {
                co = axisYDoubleValue.length;
            } else if (axisYDoubleValue.length == 0) {
                co = axisYValue.length;
            }

            while (co > 2) {
                canvas.drawLine((float) chartRect.left, (float) y, (float) chartRect.right, (float) y, barPaint);
                y += offsetY;
                co--;
            }

            // draw level values
            y = Math.round(topm + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.descent);
            int x = vw - rightm + 20;

            if (axisYValue.length == 0) {
                co = axisYDoubleValue.length;
            } else if (axisYDoubleValue.length == 0) {
                co = axisYValue.length;
            }

            // 右方Ｙ軸
            while (co > 0) {
                co--;
                String v = String.format(Locale.getDefault(), "%d", axisYValue[co]);
                canvas.drawText(v, x, y, textPaint);
                y += offsetY;
            }

            // draw data values
            y = vh - bottomm - Math.round(fontMetrics.ascent) + 20;
            x = leftm + offsetX;

            // 下方Ｘ軸
            for (int i = 0; i < dataValue.length; i++) {
                String v = axisXLable[i];
                int tw = Math.round(textPaint.measureText(v));
                int x1 = x - tw / 2;
                canvas.drawText(v, x1, y, textPaint);
                x += offsetX;
            }

            // draw color bar
            x = leftm + offsetX;
            barPaint.setStyle(Paint.Style.FILL);
            int totaldelta = (axisYValue[axisYValue.length - 1] - axisYValue[0]);

            for (int i = 0; i < dataValue.length; i++) {
                y = chartRect.bottom - calculateBarTop(dataValue[i], totaldelta);
                int x2 = x - barWidth / 2;
                barPaint.setColor(getBarColorByValue(dataValue[i]));
                canvas.drawRect(x2, y, x2 + barWidth, chartRect.bottom, barPaint);
                x += offsetX;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int calculateBarTop(int value, int total) {

        if (barColor.length < 1) return 0;

        int index = 0;
        int starty = 0;

        while (index < axisYValue.length - 1) {
            if (value >= axisYValue[index] && value < axisYValue[index + 1]) {
                return starty + (value - axisYValue[index]) * offsetY / (axisYValue[index + 1] - axisYValue[index]);
            }
            starty += offsetY;
            index++;
        }
        return 0;
    }

    public int getBarColorByValue(int v) {
        int index = 0;
        for (int i = 0; i < levelValues.length; i++) {
            if (v < levelValues[i]) break;
            index++;
        }
        if (index >= barColor.length) index = barColor.length - 1;
        return barColor[index];
    }

    public void setDataValue(List<String> list) {

        int value;
        for (int i = 0; i < list.size(); i++) {

            axisXLable[i] = list.get(i);

            value = Integer.parseInt(list.get(i));
            if (value > 999) {
                dataValue[i] = 999;
            } else {
                dataValue[i] = value;
            }
        }

        offsetX = (chartRect.width() / (dataValue.length + 1));
        barWidth = (chartRect.width() / (dataValue.length * 2 + 1));

        invalidate();
        updateTypeValues();
    }

    private void updateTypeValues() {

        offsetY = (chartRect.height() / (axisYValue.length - 1));
        labelSize = offsetY / 3f;
        if (labelSize > 120) labelSize = 120;
        if (labelSize < 24) labelSize = 24;
        textPaint.setTextSize(labelSize);
        fontMetrics = textPaint.getFontMetrics();
    }
}
