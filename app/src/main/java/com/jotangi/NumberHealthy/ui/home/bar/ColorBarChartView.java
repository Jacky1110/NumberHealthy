package com.jotangi.NumberHealthy.ui.home.bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
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
public class ColorBarChartView extends View {
    public enum Type {
        HR, SLEEP, DBP, SBP, OXYGEN, TEMPERATURE, BREATHRATE
    }

    Type type = Type.HR;

    private int[] barColor = new int[0];
    private final int[] barColor1 = {
            Color.rgb(255, 224, 97),
            Color.rgb(255, 197, 102),
            Color.rgb(253, 168, 103),
            Color.rgb(250, 141, 105),
            Color.rgb(242, 120, 109)
    };

    private final int[] barColor2 = {
            Color.rgb(138, 230, 184),
            Color.rgb(126, 169, 230),
            Color.rgb(253, 232, 157)
    };

    private final int[] barColor3 = {
            Color.rgb(250, 148, 158),
            Color.rgb(252, 226, 121),
            Color.rgb(138, 230, 184),
    };
    private final int[] barColor4 = {
            Color.rgb(126, 169, 230),
            Color.rgb(138, 230, 184),
            Color.rgb(252, 226, 121),
            Color.rgb(250, 148, 158),
    };

    // 設定相關數值時, levelValues必須比barLabel, barColor多一個, 畫圖時邏輯才不會出錯
    private int[] axisYValue = new int[0];
    private double[] axisYDoubleValue = new double[0];
    private final int[] axisYHR = {60, 95, 130, 165, 200};
    private final int[] axisYSleep = {60, 95, 130, 165, 200};
    private final int[] axisYDBP = {0, 90, 140, 160, 250};
    private final int[] axisYSBP = {0, 60, 90, 115, 200};
    private final int[] axisYOxygen = {0, 50, 75, 100};
    private final double[] axisYTemperature = {0.0, 50.0, 75.0, 100.0};
    private final int[] axisYBreathRate = {0, 50, 75, 100};

    //private int[] dataValue = {85,132,75,79,148,110,70};
    private int[] dataValue = {0, 0, 0, 0, 0, 0, 0};
    private double[] dataValueDouble = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    private final int[] dataValue2 = {120, 70, 175, 80, 115, 110, 170};
    private final String[] dateLabel = {"04/16", "04/17", "04/18", "04/19", "04/20", "04/21", "04/22"};

    private int[] levelValues = new int[0];
    private double[] levelDoubleValues = new double[0];
    private final int[] levelHR = {117, 137, 156, 176};
    private final int[] levelSleep = {117, 137, 156};
    private final int[] levelDBP = {90, 140, 160};
    private final int[] levelSBP = {60, 90, 115};
    private final int[] levelOxygen = {50, 75};
    private final double[] levelTemperature = {50.0, 75.0};
    private final int[] levelBreathRate = {50, 75};

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

    private Rect chartRect = new Rect(0, 0, 0, 0);
    private Rect axisRightRect;
    private Rect axisBottomRect;


    private Paint.FontMetrics fontMetrics;
    private Paint barPaint;
    private Paint textPaint;

    public ColorBarChartView(Context context) {
        super(context);
        init();
    }

    public ColorBarChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ColorBarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ColorBarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
            axisRightRect = new Rect(w - rightm, 0, w, h);
            axisBottomRect = new Rect(0, h - bottomm, w - rightm, h);

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

            while (co > 0) {
                co--;
                String v = String.format(Locale.getDefault(), "%d", axisYValue[co]);
                canvas.drawText(v, x, y, textPaint);
                y += offsetY;
            }

            // draw data values
            y = vh - bottomm - Math.round(fontMetrics.ascent) + 20;
            x = leftm + offsetX;

            if (type != Type.SLEEP) {
                for (int i = 0; i < dataValue.length; i++) {
                    String v = String.format(Locale.getDefault(), "%d", dataValue[i]);
                    int tw = Math.round(textPaint.measureText(v));
                    int x1 = x - tw / 2;
                    canvas.drawText(v, x1, y, textPaint);
                    x += offsetX;
                }
            } else {
                for (int i = 0; i < dataValue2.length; i++) {
                    String v = dateLabel[i];
                    int tw = Math.round(textPaint.measureText(v));
                    int x1 = x - tw / 2;
                    canvas.drawText(v, x1, y, textPaint);
                    x += offsetX;
                }
            }

            // draw color bar
            x = leftm + offsetX;
            barPaint.setStyle(Paint.Style.FILL);
            int totaldelta = (axisYValue[axisYValue.length - 1] - axisYValue[0]);
            for (int i = 0; i < dataValue2.length; i++) {
                y = chartRect.bottom - calculateBarTop(dataValue[i], totaldelta);
                int x2 = x - barWidth / 2;
                barPaint.setColor(getBarColorByValue(dataValue[i]));
                canvas.drawRect(x2, y, x2 + barWidth, chartRect.bottom, barPaint);
                x += offsetX;
            }
        } catch (Exception e) {
            Log.d("colorbarchart_view", e.toString());
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

    public void setDataValue(List<Integer> valuelist) {
        Integer[] values = valuelist.toArray(new Integer[0]);
        dataValue = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            dataValue[i] = values[i];
        }
        offsetX = (chartRect.width() / (dataValue.length + 1));
        barWidth = (chartRect.width() / (dataValue.length * 2 + 1));

        invalidate();
    }

    public void setDataDoubleValue(List<Double> valuelist) {
        Double[] values = valuelist.toArray(new Double[0]);

        dataValueDouble = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            dataValueDouble[i] = values[i];
        }
        offsetX = (chartRect.width() / (dataValueDouble.length + 1));
        barWidth = (chartRect.width() / (dataValueDouble.length * 2 + 1));

        invalidate();
    }

    private void setTypeValues(int[] color, int[] level, int[] axisy) {
        barColor = color;
        levelValues = level;
        axisYValue = axisy;
    }

    private void setTypeDoubleValues(int[] color, double[] level, double[] axisy) {
        barColor = color;
        levelDoubleValues = level;
        axisYDoubleValue = axisy;
    }

    private void updateTypeValues() {
        switch (type) {
            case SLEEP:
                setTypeValues(barColor2, levelSleep, axisYSleep);
                break;
            case DBP:
                setTypeValues(barColor1, levelDBP, axisYDBP);
                break;
            case SBP:
                setTypeValues(barColor1, levelSBP, axisYSBP);
                break;
            case OXYGEN:
                setTypeValues(barColor3, levelOxygen, axisYOxygen);
                break;
            case TEMPERATURE:
                setTypeDoubleValues(barColor3, levelTemperature, axisYTemperature);
                break;
            case BREATHRATE:
                setTypeValues(barColor3, levelBreathRate, axisYBreathRate);
                break;
            default:
                setTypeValues(barColor1, levelHR, axisYHR);
        }
        offsetY = (chartRect.height() / (axisYValue.length - 1));
        labelSize = offsetY / 3f;
        if (labelSize > 120) labelSize = 120;
        if (labelSize < 24) labelSize = 24;

        textPaint.setTextSize(labelSize);
        fontMetrics = textPaint.getFontMetrics();

    }

    public void setType(Type type) {
        this.type = type;
        updateTypeValues();
    }

}

