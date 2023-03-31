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

import java.util.Locale;

public class ChartView extends View {
    private int[] barColor = {
            Color.rgb(255, 224, 97),
            Color.rgb(255, 197, 102),
            Color.rgb(253, 168, 103),
            Color.rgb(250, 141, 105),
            Color.rgb(242, 120, 109)
    };

    private String[] barLabel = {
            "熱身放鬆",
            "脂肪燃燒",
            "心肺強化",
            "耐心強化",
            "無氧極限"
    };

    private int maxValueY = 300;
    private int minValueY = 0;
    private int stepValueY = 50;
    private int maxValueX = 24;
    private int minValueX = 0;
    private int stepValueX = 2;
    // 設定相關數值時, levelValues必須比barLabel, barColor多一個, 畫圖時邏輯才不會出錯
    private int[] axisYValue = {60, 95, 130, 165, 200};
    private int[] dataValue;
    private int[] levelValues = {117, 137, 156, 176};

    private int labelColor = Color.BLACK;
    private int valueColor = Color.BLACK;
    private int lineColor = Color.rgb(0xE0, 0xE0, 0xE0);

    private int topm;
    private int leftm;
    private int rightm;
    private int bottomm;

    private int vw;
    private int vh;
    private int barHeight;
    private int barWidth;
    private float labelSize;
    private int offsetX;
    private int offsetY;
    private int strokeWidth = 3;

    private Rect chartRect;
    private Rect axisRightRect;
    private Rect axisBottomRect;


    private Paint.FontMetrics fontMetrics;
    private Paint barPaint;
    private Paint textPaint;

    public ChartView(Context context) {
        super(context);
        init();
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
        chartRect = new Rect(leftm, topm, w - rightm, h - bottomm);
        axisRightRect = new Rect(w - rightm, 0, w, h);
        axisBottomRect = new Rect(0, h - bottomm, w - rightm, h);

        offsetY = (chartRect.height() / (axisYValue.length - 1));
        offsetX = (chartRect.width() / (dataValue.length + 1));

        barWidth = (chartRect.width() / (dataValue.length * 2 + 1));

        labelSize = offsetY / 3f;
        if (labelSize > 120) labelSize = 120;
        if (labelSize < 24) labelSize = 24;

        textPaint.setTextSize(labelSize);
        fontMetrics = textPaint.getFontMetrics();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (vw == 0 || vh == 0) return;
        if (barColor.length == 0) return;

        // draw chart rect
        barPaint.setColor(lineColor);
        barPaint.setStyle(Paint.Style.STROKE);
        barPaint.setStrokeWidth(strokeWidth);
        canvas.drawRect(chartRect, barPaint);

        // draw horizontal line in chart
        int y = topm + offsetY;
        int co = axisYValue.length;
        while (co > 2) {
            canvas.drawLine((float) chartRect.left, (float) y, (float) chartRect.right, (float) y, barPaint);
            y += offsetY;
            co--;
        }

        // draw level values
        y = Math.round(topm + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.descent);
        int x = vw - rightm + 20;
        co = axisYValue.length;
        while (co > 0) {
            co--;
            String v = String.format(Locale.getDefault(), "%d", axisYValue[co]);
            canvas.drawText(v, x, y, textPaint);
            y += offsetY;
        }

        // draw data values
        y = vh - bottomm - Math.round(fontMetrics.ascent) + 20;
        x = leftm + offsetX;
        for (int i = 0; i < dataValue.length; i++) {
            String v = String.format(Locale.getDefault(), "%d", dataValue[i]);
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
            int delta = (dataValue[i] - axisYValue[0]);
            y = chartRect.bottom - chartRect.height() * delta / totaldelta;
            int x2 = x - barWidth / 2;
            barPaint.setColor(getBarColorByValue(dataValue[i]));
            canvas.drawRect(x2, y, x2 + barWidth, chartRect.bottom, barPaint);
            x += offsetX;
        }
    }

    public int getBarColorByValue(int v) {
        int index = 0;
        for (int i = 0; i < levelValues.length; i++) {
            if (v > levelValues[i]) index++;
        }
        if (index >= barColor.length) index = barColor.length - 1;
        return barColor[index];
    }

    public void setDataValue(int[] v) {
        dataValue = v;
        invalidate();
    }

}
