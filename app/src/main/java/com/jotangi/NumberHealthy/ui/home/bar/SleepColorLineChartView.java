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

/**
 * top 16dp, left 16dp right 40dp bottom 16dp
 * 垂直間隔 2
 * 色條高 20
 * 垂直間隔 2
 * 文字高 16
 * 下間隔 2
 * 色條平均分配View的寬度
 */
public class SleepColorLineChartView extends View {

    private final int[] barColor = {
            Color.rgb(216, 208, 242),
            Color.rgb(106, 80, 181)
    };

    //private int[] dataSBP = {85,80,115,79,148,103,135};
    //private int[] dataDBP = {120,110,175,125,190,145,170};
    private int[] dataSBP = {0, 0, 0, 0, 0, 0, 0};
    private int[] dataDBP = {0, 0, 0, 0, 0, 0, 0};
    private String[] dateLabel = {"", "", "", "", "", "", ""};

    private final int[] levelDBP = {90, 140, 160};
    private final int[] levelSBP = {60, 90, 115};
    private int[] axisYValue = {0, 3, 6, 9, 12};

    private final int labelColor = Color.BLACK;
    private final int valueColor = Color.BLACK;
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
    private float density = 3f;

    private Rect chartRect = new Rect(0, 0, 0, 0);

    private Paint.FontMetrics fontMetrics;
    private Paint barPaint;
    private Paint textPaint;

    public SleepColorLineChartView(Context context) {
        super(context);
        init();
    }

    public SleepColorLineChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SleepColorLineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SleepColorLineChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        barPaint = new Paint();
        barPaint.setStyle(Paint.Style.FILL);

        textPaint = new Paint();
        textPaint.setTextSize(labelSize);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        density = displayMetrics.density;
        topm = Math.round(25 * density);
        bottomm = Math.round(80 * density);
        leftm = Math.round(50 * density);
        rightm = Math.round(16 * density);

        strokeWidth = Math.round(2 * density);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        vw = w;
        vh = h;

        if (w != 0 && h != 0) {
//            labelSize = vh / 12f;
//            if (labelSize > 60) labelSize = 60;
//            if (labelSize < 24) labelSize = 24;
            labelSize = 36;

            textPaint.setTextSize(labelSize);
            fontMetrics = textPaint.getFontMetrics();

            bottomm = Math.round(fontMetrics.descent - fontMetrics.ascent) * 2 + 24;

            chartRect = new Rect(leftm, topm, w - rightm, h - bottomm);

            offsetX = (chartRect.width() / (dataSBP.length + 1));
            barWidth = (chartRect.width() / (dataSBP.length * 2 + 1));

            offsetY = (chartRect.height() / (axisYValue.length - 1));

        }
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

        int x;

        // draw level values
        y = Math.round(topm + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.descent);
        x = rightm;


        canvas.drawText("時", x, y - x, textPaint);


        co = axisYValue.length;
        while (co > 0) {
            co--;
            String v = String.format(Locale.getDefault(), "%d", axisYValue[co]);
            canvas.drawText(v, x, y, textPaint);
            y += offsetY;
        }


        // draw dbp values
        y = vh - bottomm - Math.round(fontMetrics.ascent) + 12;
        x = leftm + offsetX;

        for (int i = 0; i < dateLabel.length; i++) {
            String v = String.format(Locale.getDefault(), "%s", dateLabel[i]);
            int tw = Math.round(textPaint.measureText(v));
            int x1 = x - tw / 2;
            canvas.drawText(dateLabel[i], x1, y, textPaint);
            x += offsetX;
        }

        // draw SBP values
//        y = y - Math.round(fontMetrics.ascent)+12;
//        x = leftm + offsetX;
//
//        for (int i = 0; i < dataSBP.length; i++) {
//            String v = String.format(Locale.getDefault(), "%d", dataSBP[i]);
//            int tw = Math.round(textPaint.measureText(v));
//            int x1 = x - tw/2;
//            canvas.drawText(v, x1, y, textPaint);
//            x += offsetX;
//        }

        // draw DBP color bar
        x = leftm + offsetX;
        barPaint.setStyle(Paint.Style.FILL);
        int rx = -1, ry = -1;
        for (int i = 0; i < dataDBP.length; i++) {
            y = chartRect.bottom - calculateBarTop(dataDBP[i]);
            int x2 = x - barWidth / 2;
            barPaint.setColor(barColor[0]);
            //canvas.drawRect(x2, y, x2+barWidth, chartRect.bottom, barPaint);
            canvas.drawCircle(x, y, 3, barPaint);
            if (rx > 0 && ry > 0) {
                canvas.drawLine(rx, ry, x, y, barPaint);
                rx = x;
                ry = y;
            } else {
                rx = x;
                ry = y;
            }
            x += offsetX;
        }

        // draw SBP color bar
        x = leftm + offsetX;
        barPaint.setStyle(Paint.Style.FILL);
        rx = -1;
        ry = -1;
        for (int i = 0; i < dataSBP.length; i++) {
            y = chartRect.bottom - calculateBarTop(dataSBP[i]);
            int x2 = x - barWidth / 2;
            barPaint.setColor(barColor[1]);
            //canvas.drawRect(x2, y, x2+barWidth, chartRect.bottom, barPaint);
            canvas.drawCircle(x, y, 3, barPaint);
            if (rx > 0 && ry > 0) {
                canvas.drawLine(rx, ry, x, y, barPaint);
                rx = x;
                ry = y;
            } else {
                rx = x;
                ry = y;
            }
            x += offsetX;
        }
    }

    private int calculateBarTop(int value) {

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

    public void setDataValue(int[] data1, int[] data2) {
        dataDBP = data1;
        dataSBP = data2;

        offsetX = (chartRect.width() / (dataSBP.length + 1));
        barWidth = (chartRect.width() / (dataSBP.length * 2 + 1));

        if (dataDBP.length != 0) {
            try {
                int[] n_axisYValue = new int[5];
                n_axisYValue[0] = 0;
                int max = 0;
                for (int i = 0; i < dataDBP.length; i++) {
                    if (dataDBP[i] > max) {
                        max = dataDBP[i];
                    }
                }
                int ld = (max + 4) / 4;
                for (int i = 1; i < n_axisYValue.length; i++) {
                    n_axisYValue[i] = n_axisYValue[i - 1] + ld;
                }

                axisYValue = n_axisYValue;
            } catch (Exception e) {

            }
        }
        invalidate();
    }

    public void setDateLabel(String[] data) {
        dateLabel = data;
    }
}
